package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineCommentService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineLikeService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineService;
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
@RequestMapping("/api/style-cop-guidelines")
public class StyleCopGuidelineController {
    private final StyleCopGuidelineService styleCopGuidelineService;
    private final StyleCopGuidelineAttachedFileService styleCopGuidelineAttachedFileService;
    private final StyleCopGuidelineCommentService styleCopGuidelineCommentService;
    private final StyleCopGuidelineLikeService styleCopGuidelineLikeService;

    public StyleCopGuidelineController(StyleCopGuidelineService styleCopGuidelineService,
                                       StyleCopGuidelineAttachedFileService styleCopGuidelineAttachedFileService,
                                       StyleCopGuidelineCommentService styleCopGuidelineCommentService,
                                       StyleCopGuidelineLikeService styleCopGuidelineLikeService) {
        this.styleCopGuidelineService = styleCopGuidelineService;
        this.styleCopGuidelineAttachedFileService = styleCopGuidelineAttachedFileService;
        this.styleCopGuidelineCommentService = styleCopGuidelineCommentService;
        this.styleCopGuidelineLikeService = styleCopGuidelineLikeService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param styleCopGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getStyleCopGuidelineList(Pageable pageable, StyleCopGuidelineSearchDto styleCopGuidelineSearchDto) {
        Page<StyleCopGuidelineDto> styleCopGuidelineDtoList = styleCopGuidelineService.findStyleCopGuidelineList(pageable, styleCopGuidelineSearchDto);

        return new ResponseEntity(styleCopGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getStyleCopGuidelineWhenRead(@PathVariable("idx") long idx) {
        StyleCopGuidelineDto styleCopGuidelineDto = styleCopGuidelineService.findStyleCopGuidelineByIdx(idx);

        styleCopGuidelineDto = styleCopGuidelineAttachedFileService.findAttachedFileByStyleCopGuidelineIdx(styleCopGuidelineDto);
        styleCopGuidelineDto = styleCopGuidelineCommentService.findAllByStyleCopGuidelineIdxOrderByIdxDesc(styleCopGuidelineDto);
        styleCopGuidelineDto = styleCopGuidelineLikeService.findStyleCopGuidelineLike(styleCopGuidelineDto);

        return new ResponseEntity(styleCopGuidelineDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStyleCopGuidelineWhenForm(@PathVariable("idx") long idx) {
        StyleCopGuidelineDto styleCopGuidelineDto = styleCopGuidelineService.findStyleCopGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (styleCopGuidelineDto.isAccess()) {
            styleCopGuidelineDto = styleCopGuidelineAttachedFileService.findAttachedFileByStyleCopGuidelineIdx(styleCopGuidelineDto);
            styleCopGuidelineDto = styleCopGuidelineService.findStyleCopGuidelineAutoComplete(styleCopGuidelineDto);
            responseEntity = new ResponseEntity(styleCopGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postStyleCopGuideline(@RequestBody @Valid StyleCopGuidelineDto styleCopGuidelineDto) {
        long idx = styleCopGuidelineService.insertStyleCopGuideline(styleCopGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param styleCopGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCopGuideline(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopGuidelineDto styleCopGuidelineDto) {
        styleCopGuidelineService.updateStyleCopGuideline(idx, styleCopGuidelineDto);

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
    public ResponseEntity deleteStyleCopGuideline(@PathVariable("idx") long idx) throws Exception {
        styleCopGuidelineService.deleteStyleCopGuidelineByIdx(idx);
        styleCopGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        styleCopGuidelineCommentService.deleteAllByStyleCopGuidelineIdx(idx);
        styleCopGuidelineLikeService.deleteAllByStyleCopGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param styleCopGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long styleCopGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        styleCopGuidelineAttachedFileService.uploadAttachedFile(styleCopGuidelineIdx, files);

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
        styleCopGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}