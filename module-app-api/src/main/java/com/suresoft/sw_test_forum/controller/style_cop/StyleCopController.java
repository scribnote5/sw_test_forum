package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopAttachedFileService;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopCommentService;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopService;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleService;
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
@RequestMapping("/api/style-cop")
public class StyleCopController {
    private final StyleCopService styleCopService;
    private final StyleCopAttachedFileService styleCopAttachedFileService;
    private final StyleCopCommentService styleCopCommentService;
    private final StyleCopExampleService styleCopExampleService;
    private final StyleCopGuidelineService styleCopGuidelineService;

    public StyleCopController(StyleCopService styleCopService,
                              StyleCopAttachedFileService styleCopAttachedFileService,
                              StyleCopCommentService styleCopCommentService,
                              StyleCopExampleService styleCopExampleService,
                              StyleCopGuidelineService styleCopGuidelineService) {
        this.styleCopService = styleCopService;
        this.styleCopAttachedFileService = styleCopAttachedFileService;
        this.styleCopCommentService = styleCopCommentService;
        this.styleCopExampleService = styleCopExampleService;
        this.styleCopGuidelineService = styleCopGuidelineService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getStyleCopListByPriorityAsc() {
        List<StyleCopDto> styleCopDtoList = styleCopService.findAllByHighPriorityAsc();

        return new ResponseEntity(styleCopDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param styleCopSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getStyleCopList(Pageable pageable, StyleCopSearchDto styleCopSearchDto) {
        Page<StyleCopDto> styleCopDtoList = styleCopService.findAll(pageable, styleCopSearchDto);

        return new ResponseEntity(styleCopDtoList, HttpStatus.OK);
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
    public ResponseEntity getStyleCopWhenRead(@PathVariable("idx") long idx) {
        StyleCopDto styleCopDto = styleCopService.findStyleCopByIdx(idx);

        styleCopDto = styleCopAttachedFileService.findAttachedFileByStyleCopIdx(styleCopDto);
        styleCopDto = styleCopCommentService.findAllByStyleCopIdxOrderByIdxDesc(styleCopDto);
        styleCopDto = styleCopExampleService.findStyleCopExampleList(idx, styleCopDto);
        styleCopDto = styleCopGuidelineService.findStyleCopGuidelineList(idx, styleCopDto);

        return new ResponseEntity(styleCopDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/style-cop-rule/{idx}"})
    public ResponseEntity getStyleCopRule(@PathVariable("idx") long idx) {
        String styleCopRule = styleCopService.findStyleCopRuleByIdx(idx);

        return new ResponseEntity(styleCopRule, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStyleCopWhenForm(@PathVariable("idx") long idx) {
        StyleCopDto styleCopDto = styleCopService.findStyleCopByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (styleCopDto.isAccess()) {
            styleCopDto = styleCopAttachedFileService.findAttachedFileByStyleCopIdx(styleCopDto);
            styleCopDto = styleCopService.findStyleCopAutoComplete(styleCopDto);
            responseEntity = new ResponseEntity(styleCopDto, HttpStatus.OK);
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
        return new ResponseEntity(styleCopService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(styleCopService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopDto
     * @return
     */
    @PostMapping
    public ResponseEntity postStyleCop(@RequestBody @Valid StyleCopDto styleCopDto) {
        long idx = styleCopService.insertStyleCop(styleCopDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param styleCopDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCop(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopDto styleCopDto) {
        styleCopService.updateStyleCop(idx, styleCopDto);

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
    public ResponseEntity deleteStyleCop(@PathVariable("idx") long idx) throws Exception {
        styleCopService.deleteRelatedStyleCopByIdx(idx);
        styleCopAttachedFileService.deleteAllAttachedFile(idx);
        styleCopCommentService.deleteAllByStyleCopIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param styleCopIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long styleCopIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        styleCopAttachedFileService.uploadAttachedFile(styleCopIdx, files);

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
        styleCopAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}