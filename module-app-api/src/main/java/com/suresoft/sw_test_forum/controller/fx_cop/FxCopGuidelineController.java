package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineCommentService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineLikeService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineService;
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
@RequestMapping("/api/fx-cop-guidelines")
public class FxCopGuidelineController {
    private final FxCopGuidelineService fxCopGuidelineService;
    private final FxCopGuidelineAttachedFileService fxCopGuidelineAttachedFileService;
    private final FxCopGuidelineCommentService fxCopGuidelineCommentService;
    private final FxCopGuidelineLikeService fxCopGuidelineLikeService;

    public FxCopGuidelineController(FxCopGuidelineService fxCopGuidelineService,
                                    FxCopGuidelineAttachedFileService fxCopGuidelineAttachedFileService,
                                    FxCopGuidelineCommentService fxCopGuidelineCommentService,
                                    FxCopGuidelineLikeService fxCopGuidelineLikeService) {
        this.fxCopGuidelineService = fxCopGuidelineService;
        this.fxCopGuidelineAttachedFileService = fxCopGuidelineAttachedFileService;
        this.fxCopGuidelineCommentService = fxCopGuidelineCommentService;
        this.fxCopGuidelineLikeService = fxCopGuidelineLikeService;
    }

    /**
     * ????????? ???????????????, ????????? ??????
     *
     * @param pageable
     * @param fxCopGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getFxCopGuidelineList(Pageable pageable, FxCopGuidelineSearchDto fxCopGuidelineSearchDto) {
        Page<FxCopGuidelineDto> fxCopGuidelineDtoList = fxCopGuidelineService.findFxCopGuidelineList(pageable, fxCopGuidelineSearchDto);

        return new ResponseEntity(fxCopGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getFxCopGuidelineWhenRead(@PathVariable("idx") long idx) {
        FxCopGuidelineDto fxCopGuidelineDto = fxCopGuidelineService.findFxCopGuidelineByIdx(idx);

        fxCopGuidelineDto = fxCopGuidelineAttachedFileService.findAttachedFileByFxCopGuidelineIdx(fxCopGuidelineDto);
        fxCopGuidelineDto = fxCopGuidelineCommentService.findAllByFxCopGuidelineIdxOrderByIdxDesc(fxCopGuidelineDto);
        fxCopGuidelineDto = fxCopGuidelineLikeService.findFxCopGuidelineLike(fxCopGuidelineDto);

        return new ResponseEntity(fxCopGuidelineDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getFxCopGuidelineWhenForm(@PathVariable("idx") long idx) {
        FxCopGuidelineDto fxCopGuidelineDto = fxCopGuidelineService.findFxCopGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (fxCopGuidelineDto.isAccess()) {
            fxCopGuidelineDto = fxCopGuidelineAttachedFileService.findAttachedFileByFxCopGuidelineIdx(fxCopGuidelineDto);
            fxCopGuidelineDto = fxCopGuidelineService.findFxCopGuidelineAutoComplete(fxCopGuidelineDto);
            responseEntity = new ResponseEntity(fxCopGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param fxCopGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postFxCopGuideline(@RequestBody @Valid FxCopGuidelineDto fxCopGuidelineDto) {
        long idx = fxCopGuidelineService.insertFxCopGuideline(fxCopGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param fxCopGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCopGuideline(@PathVariable("idx") long idx, @RequestBody @Valid FxCopGuidelineDto fxCopGuidelineDto) {
        fxCopGuidelineService.updateFxCopGuideline(idx, fxCopGuidelineDto);

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
    public ResponseEntity deleteFxCopGuideline(@PathVariable("idx") long idx) throws Exception {
        fxCopGuidelineService.deleteFxCopGuidelineByIdx(idx);
        fxCopGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        fxCopGuidelineCommentService.deleteAllByFxCopGuidelineIdx(idx);
        fxCopGuidelineLikeService.deleteAllByFxCopGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param fxCopGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long fxCopGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        fxCopGuidelineAttachedFileService.uploadAttachedFile(fxCopGuidelineIdx, files);

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
        fxCopGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}