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
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getMisraCListByPriorityAsc() {
        List<MisraCDto> misraCDtoList = misraCService.findAllByHighPriorityAsc();

        return new ResponseEntity(misraCDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
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
    public ResponseEntity getMisraCWhenRead(@PathVariable("idx") long idx) {
        MisraCDto misraCDto = misraCService.findMisraCByIdx(idx);

        misraCDto = misraCAttachedFileService.findAttachedFileByMisraCIdx(misraCDto);
        misraCDto = misraCCommentService.findAllByMisraCIdxOrderByIdxDesc(misraCDto);
        misraCDto = misraCExampleService.findMisraCExampleList(idx, misraCDto);
        misraCDto = misraCGuidelineService.findMisraCGuidelineList(idx, misraCDto);

        return new ResponseEntity(misraCDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, MISRA C ?????? ??????
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
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCWhenForm(@PathVariable("idx") long idx) {
        MisraCDto misraCDto = misraCService.findMisraCByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (misraCDto.isAccess()) {
            misraCDto = misraCAttachedFileService.findAttachedFileByMisraCIdx(misraCDto);
            misraCDto = misraCService.findMisraCAutoComplete(misraCDto);
            responseEntity = new ResponseEntity(misraCDto, HttpStatus.OK);
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
        return new ResponseEntity(misraCService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(misraCService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param misraCIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long misraCIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        misraCAttachedFileService.uploadAttachedFile(misraCIdx, files);

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
        misraCAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}