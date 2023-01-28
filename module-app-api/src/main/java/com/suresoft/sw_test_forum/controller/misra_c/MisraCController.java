package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCAttachedFileService;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCCommentService;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCService;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineService;
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
@RequestMapping("/api/misra-c")
public class MisraCController {
    private final MisraCService misraCService;
    private final MisraCAttachedFileService misraCAttachedFileService;
    private final MisraCCommentService misraCCommentService;
    private final MisraCExampleService misraCExampleService;
    private final MisraCGuidelineService misraCGuidelineService;

    public MisraCController(MisraCService misraCService,
                            MisraCAttachedFileService misraCAttachedFileService,
                            MisraCCommentService misraCCommentService,
                            MisraCExampleService misraCExampleService,
                            MisraCGuidelineService misraCGuidelineService) {
        this.misraCService = misraCService;
        this.misraCAttachedFileService = misraCAttachedFileService;
        this.misraCCommentService = misraCCommentService;
        this.misraCExampleService = misraCExampleService;
        this.misraCGuidelineService = misraCGuidelineService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getMisraCListByPriorityAsc() {
        List<MisraCDto> misraCDtoList = misraCService.findAllByHighPriorityAsc();

        return new ResponseEntity(misraCDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param misraCSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCList(Pageable pageable, MisraCSearchDto misraCSearchDto) {
        Page<MisraCDto> misraCDtoList = misraCService.findAll(pageable, misraCSearchDto);

        return new ResponseEntity(misraCDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCWhenRead(@PathVariable("idx") long idx) {
        MisraCDto misraCDto = misraCService.findMisraCByIdx(idx);

        misraCDto = misraCAttachedFileService.findAttachedFileByMisraCIdx(misraCDto);
        misraCDto = misraCCommentService.findAllByMisraCIdxOrderByIdxDesc(misraCDto);
        misraCDto = misraCExampleService.findMisraCExampleList(idx, misraCDto);
        misraCDto = misraCGuidelineService.findMisraCGuidelineList(idx, misraCDto);

        return new ResponseEntity(misraCDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/misra-c-rule/{idx}"})
    public ResponseEntity getMisraCRule(@PathVariable("idx") long idx) {
        String misraCRule = misraCService.findMisraCRuleByIdx(idx);

        return new ResponseEntity(misraCRule, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCWhenForm(@PathVariable("idx") long idx) {
        MisraCDto misraCDto = misraCService.findMisraCByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (misraCDto.isAccess()) {
            misraCDto = misraCAttachedFileService.findAttachedFileByMisraCIdx(misraCDto);
            misraCDto = misraCService.findMisraCAutoComplete(misraCDto);
            responseEntity = new ResponseEntity(misraCDto, HttpStatus.OK);
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
        return new ResponseEntity(misraCService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(misraCService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraC(@RequestBody @Valid MisraCDto misraCDto) {
        long idx = misraCService.insertMisraC(misraCDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraC(@PathVariable("idx") long idx, @RequestBody @Valid MisraCDto misraCDto) {
        misraCService.updateMisraC(idx, misraCDto);

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
    public ResponseEntity deleteMisraC(@PathVariable("idx") long idx) throws Exception {
        misraCService.deleteRelatedMisraCByIdx(idx);
        misraCAttachedFileService.deleteAllAttachedFile(idx);
        misraCCommentService.deleteAllByMisraCIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param misraCIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long misraCIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        misraCAttachedFileService.uploadAttachedFile(misraCIdx, files);

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
        misraCAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}