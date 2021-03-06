package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppAttachedFileService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppCommentService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleService;
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
@RequestMapping("/api/misra-cpp")
public class MisraCppController {
    private final MisraCppService misraCppService;
    private final MisraCppAttachedFileService misraCppAttachedFileService;
    private final MisraCppCommentService misraCppCommentService;
    private final MisraCppExampleService misraCppExampleService;
    private final MisraCppGuidelineService misraCppGuidelineService;

    public MisraCppController(MisraCppService misraCppService,
                              MisraCppAttachedFileService misraCppAttachedFileService,
                              MisraCppCommentService misraCppCommentService,
                              MisraCppExampleService misraCppExampleService,
                              MisraCppGuidelineService misraCppGuidelineService) {
        this.misraCppService = misraCppService;
        this.misraCppAttachedFileService = misraCppAttachedFileService;
        this.misraCppCommentService = misraCppCommentService;
        this.misraCppExampleService = misraCppExampleService;
        this.misraCppGuidelineService = misraCppGuidelineService;
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getMisraCppListByPriorityAsc() {
        List<MisraCppDto> misraCppDtoList = misraCppService.findAllByHighPriorityAsc();

        return new ResponseEntity(misraCppDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param misraCppSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCppList(Pageable pageable, MisraCppSearchDto misraCppSearchDto) {
        Page<MisraCppDto> misraCppDtoList = misraCppService.findAll(pageable, misraCppSearchDto);

        return new ResponseEntity(misraCppDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCppWhenRead(@PathVariable("idx") long idx) {
        MisraCppDto misraCppDto = misraCppService.findMisraCppByIdx(idx);

        misraCppDto = misraCppAttachedFileService.findAttachedFileByMisraCppIdx(misraCppDto);
        misraCppDto = misraCppCommentService.findAllByMisraCppIdxOrderByIdxDesc(misraCppDto);
        misraCppDto = misraCppExampleService.findMisraCppExampleList(idx, misraCppDto);
        misraCppDto = misraCppGuidelineService.findMisraCppGuidelineList(idx, misraCppDto);

        return new ResponseEntity(misraCppDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, MISRA CPP ?????? ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/misra-cpp-rule/{idx}"})
    public ResponseEntity getMisraCppRule(@PathVariable("idx") long idx) {
        String misraCppRule = misraCppService.findMisraCppRuleByIdx(idx);

        return new ResponseEntity(misraCppRule, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCppWhenForm(@PathVariable("idx") long idx) {
        MisraCppDto misraCppDto = misraCppService.findMisraCppByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (misraCppDto.isAccess()) {
            misraCppDto = misraCppAttachedFileService.findAttachedFileByMisraCppIdx(misraCppDto);
            misraCppDto = misraCppService.findMisraCppAutoComplete(misraCppDto);
            responseEntity = new ResponseEntity(misraCppDto, HttpStatus.OK);
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
        return new ResponseEntity(misraCppService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(misraCppService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param misraCppDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraCpp(@RequestBody @Valid MisraCppDto misraCppDto) {
        long idx = misraCppService.insertMisraCpp(misraCppDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param misraCppDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCpp(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppDto misraCppDto) {
        misraCppService.updateMisraCpp(idx, misraCppDto);

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
    public ResponseEntity deleteMisraCpp(@PathVariable("idx") long idx) throws Exception {
        misraCppService.deleteRelatedMisraCppByIdx(idx);
        misraCppAttachedFileService.deleteAllAttachedFile(idx);
        misraCppCommentService.deleteAllByMisraCppIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param misraCppIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long misraCppIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        misraCppAttachedFileService.uploadAttachedFile(misraCppIdx, files);

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
        misraCppAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}