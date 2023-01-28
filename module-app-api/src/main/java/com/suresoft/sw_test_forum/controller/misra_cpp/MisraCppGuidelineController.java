package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineCommentService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineLikeService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineService;
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
@RequestMapping("/api/misra-cpp-guidelines")
public class MisraCppGuidelineController {
    private final MisraCppGuidelineService misraCppGuidelineService;
    private final MisraCppGuidelineAttachedFileService misraCppGuidelineAttachedFileService;
    private final MisraCppGuidelineCommentService misraCppGuidelineCommentService;
    private final MisraCppGuidelineLikeService misraCppGuidelineLikeService;

    public MisraCppGuidelineController(MisraCppGuidelineService misraCppGuidelineService,
                                       MisraCppGuidelineAttachedFileService misraCppGuidelineAttachedFileService,
                                       MisraCppGuidelineCommentService misraCppGuidelineCommentService,
                                       MisraCppGuidelineLikeService misraCppGuidelineLikeService) {
        this.misraCppGuidelineService = misraCppGuidelineService;
        this.misraCppGuidelineAttachedFileService = misraCppGuidelineAttachedFileService;
        this.misraCppGuidelineCommentService = misraCppGuidelineCommentService;
        this.misraCppGuidelineLikeService = misraCppGuidelineLikeService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param misraCppGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCppGuidelineList(Pageable pageable, MisraCppGuidelineSearchDto misraCppGuidelineSearchDto) {
        Page<MisraCppGuidelineDto> misraCppGuidelineDtoList = misraCppGuidelineService.findMisraCppGuidelineList(pageable, misraCppGuidelineSearchDto);

        return new ResponseEntity(misraCppGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCppGuidelineWhenRead(@PathVariable("idx") long idx) {
        MisraCppGuidelineDto misraCppGuidelineDto = misraCppGuidelineService.findMisraCppGuidelineByIdx(idx);

        misraCppGuidelineDto = misraCppGuidelineAttachedFileService.findAttachedFileByMisraCppGuidelineIdx(misraCppGuidelineDto);
        misraCppGuidelineDto = misraCppGuidelineCommentService.findAllByMisraCppGuidelineIdxOrderByIdxDesc(misraCppGuidelineDto);
        misraCppGuidelineDto = misraCppGuidelineLikeService.findMisraCppGuidelineLike(misraCppGuidelineDto);

        return new ResponseEntity(misraCppGuidelineDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCppGuidelineWhenForm(@PathVariable("idx") long idx) {
        MisraCppGuidelineDto misraCppGuidelineDto = misraCppGuidelineService.findMisraCppGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (misraCppGuidelineDto.isAccess()) {
            misraCppGuidelineDto = misraCppGuidelineAttachedFileService.findAttachedFileByMisraCppGuidelineIdx(misraCppGuidelineDto);
            misraCppGuidelineDto = misraCppGuidelineService.findMisraCppGuidelineAutoComplete(misraCppGuidelineDto);
            responseEntity = new ResponseEntity(misraCppGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCppGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraCppGuideline(@RequestBody @Valid MisraCppGuidelineDto misraCppGuidelineDto) {
        long idx = misraCppGuidelineService.insertMisraCppGuideline(misraCppGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCppGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCppGuideline(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppGuidelineDto misraCppGuidelineDto) {
        misraCppGuidelineService.updateMisraCppGuideline(idx, misraCppGuidelineDto);

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
    public ResponseEntity deleteMisraCppGuideline(@PathVariable("idx") long idx) throws Exception {
        misraCppGuidelineService.deleteMisraCppGuidelineByIdx(idx);
        misraCppGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        misraCppGuidelineCommentService.deleteAllByMisraCppGuidelineIdx(idx);
        misraCppGuidelineLikeService.deleteAllByMisraCppGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param misraCppGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long misraCppGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        misraCppGuidelineAttachedFileService.uploadAttachedFile(misraCppGuidelineIdx, files);

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
        misraCppGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}