package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopAttachedFileService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopCommentService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleService;
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
@RequestMapping("/api/fx-cop")
public class FxCopController {
    private final FxCopService fxCopService;
    private final FxCopAttachedFileService fxCopAttachedFileService;
    private final FxCopCommentService fxCopCommentService;
    private final FxCopExampleService fxCopExampleService;
    private final FxCopGuidelineService fxCopGuidelineService;

    public FxCopController(FxCopService fxCopService,
                           FxCopAttachedFileService fxCopAttachedFileService,
                           FxCopCommentService fxCopCommentService,
                           FxCopExampleService fxCopExampleService,
                           FxCopGuidelineService fxCopGuidelineService) {
        this.fxCopService = fxCopService;
        this.fxCopAttachedFileService = fxCopAttachedFileService;
        this.fxCopCommentService = fxCopCommentService;
        this.fxCopExampleService = fxCopExampleService;
        this.fxCopGuidelineService = fxCopGuidelineService;
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getFxCopListByPriorityAsc() {
        List<FxCopDto> fxCopDtoList = fxCopService.findAllByHighPriorityAsc();

        return new ResponseEntity(fxCopDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param fxCopSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getFxCopList(Pageable pageable, FxCopSearchDto fxCopSearchDto) {
        Page<FxCopDto> fxCopDtoList = fxCopService.findAll(pageable, fxCopSearchDto);

        return new ResponseEntity(fxCopDtoList, HttpStatus.OK);
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
    public ResponseEntity getFxCopWhenRead(@PathVariable("idx") long idx) {
        FxCopDto fxCopDto = fxCopService.findFxCopByIdx(idx);

        fxCopDto = fxCopAttachedFileService.findAttachedFileByFxCopIdx(fxCopDto);
        fxCopDto = fxCopCommentService.findAllByFxCopIdxOrderByIdxDesc(fxCopDto);
        fxCopDto = fxCopExampleService.findFxCopExampleList(idx, fxCopDto);
        fxCopDto = fxCopGuidelineService.findFxCopGuidelineList(idx, fxCopDto);

        return new ResponseEntity(fxCopDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, FxCop ?????? ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/fx-cop-rule/{idx}"})
    public ResponseEntity getFxCopRule(@PathVariable("idx") long idx) {
        String fxCopRule = fxCopService.findFxCopRuleByIdx(idx);

        return new ResponseEntity(fxCopRule, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getFxCopWhenForm(@PathVariable("idx") long idx) {
        FxCopDto fxCopDto = fxCopService.findFxCopByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (fxCopDto.isAccess()) {
            fxCopDto = fxCopAttachedFileService.findAttachedFileByFxCopIdx(fxCopDto);
            fxCopDto = fxCopService.findFxCopAutoComplete(fxCopDto);
            responseEntity = new ResponseEntity(fxCopDto, HttpStatus.OK);
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
        return new ResponseEntity(fxCopService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(fxCopService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param fxCopDto
     * @return
     */
    @PostMapping
    public ResponseEntity postFxCop(@RequestBody @Valid FxCopDto fxCopDto) {
        long idx = fxCopService.insertFxCop(fxCopDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param fxCopDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCop(@PathVariable("idx") long idx, @RequestBody @Valid FxCopDto fxCopDto) {
        fxCopService.updateFxCop(idx, fxCopDto);

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
    public ResponseEntity deleteFxCop(@PathVariable("idx") long idx) throws Exception {
        fxCopService.deleteRelatedFxCopByIdx(idx);
        fxCopAttachedFileService.deleteAllAttachedFile(idx);
        fxCopCommentService.deleteAllByFxCopIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param fxCopIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long fxCopIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        fxCopAttachedFileService.uploadAttachedFile(fxCopIdx, files);

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
        fxCopAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}