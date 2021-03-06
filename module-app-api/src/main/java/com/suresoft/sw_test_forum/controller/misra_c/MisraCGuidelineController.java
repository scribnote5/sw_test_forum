package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineCommentService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineLikeService;
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
@RequestMapping("/api/misra-c-guidelines")
public class MisraCGuidelineController {
    private final MisraCGuidelineService misraCGuidelineService;
    private final MisraCGuidelineAttachedFileService misraCGuidelineAttachedFileService;
    private final MisraCGuidelineCommentService misraCGuidelineCommentService;
    private final MisraCGuidelineLikeService misraCGuidelineLikeService;

    public MisraCGuidelineController(MisraCGuidelineService misraCGuidelineService,
                                     MisraCGuidelineAttachedFileService misraCGuidelineAttachedFileService,
                                     MisraCGuidelineCommentService misraCGuidelineCommentService,
                                     MisraCGuidelineLikeService misraCGuidelineLikeService) {
        this.misraCGuidelineService = misraCGuidelineService;
        this.misraCGuidelineAttachedFileService = misraCGuidelineAttachedFileService;
        this.misraCGuidelineCommentService = misraCGuidelineCommentService;
        this.misraCGuidelineLikeService = misraCGuidelineLikeService;
    }

    /**
     * ????????? ???????????????, ????????? ??????
     *
     * @param pageable
     * @param misraCGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCGuidelineList(Pageable pageable, MisraCGuidelineSearchDto misraCGuidelineSearchDto) {
        Page<MisraCGuidelineDto> misraCGuidelineDtoList = misraCGuidelineService.findMisraCGuidelineList(pageable, misraCGuidelineSearchDto);

        return new ResponseEntity(misraCGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCGuidelineWhenRead(@PathVariable("idx") long idx) {
        MisraCGuidelineDto misraCGuidelineDto = misraCGuidelineService.findMisraCGuidelineByIdx(idx);

        misraCGuidelineDto = misraCGuidelineAttachedFileService.findAttachedFileByMisraCGuidelineIdx(misraCGuidelineDto);
        misraCGuidelineDto = misraCGuidelineCommentService.findAllByMisraCGuidelineIdxOrderByIdxDesc(misraCGuidelineDto);
        misraCGuidelineDto = misraCGuidelineLikeService.findMisraCGuidelineLike(misraCGuidelineDto);

        return new ResponseEntity(misraCGuidelineDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCGuidelineWhenForm(@PathVariable("idx") long idx) {
        MisraCGuidelineDto misraCGuidelineDto = misraCGuidelineService.findMisraCGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (misraCGuidelineDto.isAccess()) {
            misraCGuidelineDto = misraCGuidelineAttachedFileService.findAttachedFileByMisraCGuidelineIdx(misraCGuidelineDto);
            misraCGuidelineDto = misraCGuidelineService.findMisraCGuidelineAutoComplete(misraCGuidelineDto);
            responseEntity = new ResponseEntity(misraCGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param misraCGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraCGuideline(@RequestBody @Valid MisraCGuidelineDto misraCGuidelineDto) {
        long idx = misraCGuidelineService.insertMisraCGuideline(misraCGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param misraCGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCGuideline(@PathVariable("idx") long idx, @RequestBody @Valid MisraCGuidelineDto misraCGuidelineDto) {
        misraCGuidelineService.updateMisraCGuideline(idx, misraCGuidelineDto);

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
    public ResponseEntity deleteMisraCGuideline(@PathVariable("idx") long idx) throws Exception {
        misraCGuidelineService.deleteMisraCGuidelineByIdx(idx);
        misraCGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        misraCGuidelineCommentService.deleteAllByMisraCGuidelineIdx(idx);
        misraCGuidelineLikeService.deleteAllByMisraCGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param misraCGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long misraCGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        misraCGuidelineAttachedFileService.uploadAttachedFile(misraCGuidelineIdx, files);

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
        misraCGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}