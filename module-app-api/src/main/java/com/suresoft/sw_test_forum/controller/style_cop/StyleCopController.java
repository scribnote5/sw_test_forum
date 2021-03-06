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
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getStyleCopListByPriorityAsc() {
        List<StyleCopDto> styleCopDtoList = styleCopService.findAllByHighPriorityAsc();

        return new ResponseEntity(styleCopDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
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
     * ????????? ???????????????, ?????? ?????? ??????
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRegister();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, MISRA C ?????? ??????
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
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStyleCopWhenForm(@PathVariable("idx") long idx) {
        StyleCopDto styleCopDto = styleCopService.findStyleCopByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (styleCopDto.isAccess()) {
            styleCopDto = styleCopAttachedFileService.findAttachedFileByStyleCopIdx(styleCopDto);
            styleCopDto = styleCopService.findStyleCopAutoComplete(styleCopDto);
            responseEntity = new ResponseEntity(styleCopDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-write"})
    public ResponseEntity getPriorityListWhenWrite() {
        return new ResponseEntity(styleCopService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(styleCopService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param styleCopIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long styleCopIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        styleCopAttachedFileService.uploadAttachedFile(styleCopIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ???????????? ??????
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