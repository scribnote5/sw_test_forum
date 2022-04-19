package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweAttachedFileService;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweCommentService;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineService;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cwe")
public class CweController {
    private final CweService cweService;
    private final CweAttachedFileService cweAttachedFileService;
    private final CweCommentService cweCommentService;
    private final CweExampleService cweExampleService;
    private final CweGuidelineService cweGuidelineService;

    public CweController(CweService cweService,
                         CweAttachedFileService cweAttachedFileService,
                         CweCommentService cweCommentService,
                         CweExampleService cweExampleService,
                         CweGuidelineService cweGuidelineService) {
        this.cweService = cweService;
        this.cweAttachedFileService = cweAttachedFileService;
        this.cweCommentService = cweCommentService;
        this.cweExampleService = cweExampleService;
        this.cweGuidelineService = cweGuidelineService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getCweListByPriorityAsc() {
        List<CweDto> cweDtoList = cweService.findAllByHighPriorityAsc();

        return new ResponseEntity(cweDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param cweSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweList(Pageable pageable, CweSearchDto cweSearchDto) {
        Page<CweDto> cweDtoList = cweService.findAll(pageable, cweSearchDto);

        return new ResponseEntity(cweDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 접근 권한 조회
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRegister();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getCweWhenRead(@PathVariable("idx") long idx) {
        CweDto cweDto = cweService.findCweByIdx(idx);

        cweDto = cweAttachedFileService.findAttachedFileByCweIdx(cweDto);
        cweDto = cweCommentService.findAllByCweIdxOrderByIdxDesc(cweDto);
        cweDto = cweExampleService.findCweExampleList(idx, cweDto);
        cweDto = cweGuidelineService.findCweGuidelineList(idx, cweDto);

        return new ResponseEntity(cweDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, CWE 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/cwe-rule/{idx}"})
    public ResponseEntity getCweRule(@PathVariable("idx") long idx) {
        String cweRule = cweService.findCweRuleByIdx(idx);

        return new ResponseEntity(cweRule, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweWhenForm(@PathVariable("idx") long idx) {
        CweDto cweDto = cweService.findCweByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (cweDto.isAccess()) {
            cweDto = cweAttachedFileService.findAttachedFileByCweIdx(cweDto);
            cweDto = cweService.findCweAutoComplete(cweDto);
            responseEntity = new ResponseEntity(cweDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-write"})
    public ResponseEntity getPriorityListWhenWrite() {
        return new ResponseEntity(cweService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(cweService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCwe(@RequestBody @Valid CweDto cweDto) {
        long idx = cweService.insertCwe(cweDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCwe(@PathVariable("idx") long idx, @RequestBody @Valid CweDto cweDto) {
        cweService.updateCwe(idx, cweDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteCwe(@PathVariable("idx") long idx) throws Exception {
        cweService.deleteRelatedCweByIdx(idx);
        cweAttachedFileService.deleteAllAttachedFile(idx);
        cweCommentService.deleteAllByCweIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param cweIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long cweIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        cweAttachedFileService.uploadAttachedFile(cweIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * 삭제 페이지에서, 첨부파일 삭제
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        cweAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}