package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineLikeService;
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
@RequestMapping("/api/cwe-guidelines")
public class CweGuidelineController {
    private final CweGuidelineService cweGuidelineService;
    private final CweGuidelineAttachedFileService cweGuidelineAttachedFileService;
    private final CweGuidelineCommentService cweGuidelineCommentService;
    private final CweGuidelineLikeService cweGuidelineLikeService;

    public CweGuidelineController(CweGuidelineService cweGuidelineService,
                                  CweGuidelineAttachedFileService cweGuidelineAttachedFileService,
                                  CweGuidelineCommentService cweGuidelineCommentService,
                                  CweGuidelineLikeService cweGuidelineLikeService) {
        this.cweGuidelineService = cweGuidelineService;
        this.cweGuidelineAttachedFileService = cweGuidelineAttachedFileService;
        this.cweGuidelineCommentService = cweGuidelineCommentService;
        this.cweGuidelineLikeService = cweGuidelineLikeService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param cweGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweGuidelineList(Pageable pageable, CweGuidelineSearchDto cweGuidelineSearchDto) {
        Page<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineService.findCweGuidelineList(pageable, cweGuidelineSearchDto);

        return new ResponseEntity(cweGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getCweGuidelineWhenRead(@PathVariable("idx") long idx) {
        CweGuidelineDto cweGuidelineDto = cweGuidelineService.findCweGuidelineByIdx(idx);

        cweGuidelineDto = cweGuidelineAttachedFileService.findAttachedFileByCweGuidelineIdx(cweGuidelineDto);
        cweGuidelineDto = cweGuidelineCommentService.findAllByCweGuidelineIdxOrderByIdxDesc(cweGuidelineDto);
        cweGuidelineDto = cweGuidelineLikeService.findCweGuidelineLike(cweGuidelineDto);

        return new ResponseEntity(cweGuidelineDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweGuidelineWhenForm(@PathVariable("idx") long idx) {
        CweGuidelineDto cweGuidelineDto = cweGuidelineService.findCweGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (cweGuidelineDto.isAccess()) {
            cweGuidelineDto = cweGuidelineAttachedFileService.findAttachedFileByCweGuidelineIdx(cweGuidelineDto);
            cweGuidelineDto = cweGuidelineService.findCweGuidelineAutoComplete(cweGuidelineDto);
            responseEntity = new ResponseEntity(cweGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweGuideline(@RequestBody @Valid CweGuidelineDto cweGuidelineDto) {
        long idx = cweGuidelineService.insertCweGuideline(cweGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweGuideline(@PathVariable("idx") long idx, @RequestBody @Valid CweGuidelineDto cweGuidelineDto) {
        cweGuidelineService.updateCweGuideline(idx, cweGuidelineDto);

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
    public ResponseEntity deleteCweGuideline(@PathVariable("idx") long idx) throws Exception {
        cweGuidelineService.deleteCweGuidelineByIdx(idx);
        cweGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        cweGuidelineCommentService.deleteAllByCweGuidelineIdx(idx);
        cweGuidelineLikeService.deleteAllByCweGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param cweGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long cweGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        cweGuidelineAttachedFileService.uploadAttachedFile(cweGuidelineIdx, files);

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
        cweGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}