package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaAttachedFileService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineService;
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
@RequestMapping("/api/cwe-java")
public class CweJavaController {
    private final CweJavaService cweJavaService;
    private final CweJavaAttachedFileService cweJavaAttachedFileService;
    private final CweJavaCommentService cweJavaCommentService;
    private final CweJavaExampleService cweJavaExampleService;
    private final CweJavaGuidelineService cweJavaGuidelineService;

    public CweJavaController(CweJavaService cweJavaService,
                             CweJavaAttachedFileService cweJavaAttachedFileService,
                             CweJavaCommentService cweJavaCommentService,
                             CweJavaExampleService cweJavaExampleService,
                             CweJavaGuidelineService cweJavaGuidelineService) {
        this.cweJavaService = cweJavaService;
        this.cweJavaAttachedFileService = cweJavaAttachedFileService;
        this.cweJavaCommentService = cweJavaCommentService;
        this.cweJavaExampleService = cweJavaExampleService;
        this.cweJavaGuidelineService = cweJavaGuidelineService;
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getCweJavaListByPriorityAsc() {
        List<CweJavaDto> cweJavaDtoList = cweJavaService.findAllByHighPriorityAsc();

        return new ResponseEntity(cweJavaDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param cweJavaSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweJavaList(Pageable pageable, CweJavaSearchDto cweJavaSearchDto) {
        Page<CweJavaDto> cweJavaDtoList = cweJavaService.findAll(pageable, cweJavaSearchDto);

        return new ResponseEntity(cweJavaDtoList, HttpStatus.OK);
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
    public ResponseEntity getCweJavaWhenRead(@PathVariable("idx") long idx) {
        CweJavaDto cweJavaDto = cweJavaService.findCweJavaByIdx(idx);

        cweJavaDto = cweJavaAttachedFileService.findAttachedFileByCweJavaIdx(cweJavaDto);
        cweJavaDto = cweJavaCommentService.findAllByCweJavaIdxOrderByIdxDesc(cweJavaDto);
        cweJavaDto = cweJavaExampleService.findCweJavaExampleList(idx, cweJavaDto);
        cweJavaDto = cweJavaGuidelineService.findCweJavaGuidelineList(idx, cweJavaDto);

        return new ResponseEntity(cweJavaDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, CWE ?????? ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/cwe-java-rule/{idx}"})
    public ResponseEntity getCweJavaRule(@PathVariable("idx") long idx) {
        String cweRule = cweJavaService.findCweJavaRuleByIdx(idx);

        return new ResponseEntity(cweRule, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweJavaWhenForm(@PathVariable("idx") long idx) {
        CweJavaDto cweJavaDto = cweJavaService.findCweJavaByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (cweJavaDto.isAccess()) {
            cweJavaDto = cweJavaAttachedFileService.findAttachedFileByCweJavaIdx(cweJavaDto);
            cweJavaDto = cweJavaService.findCweJavaAutoComplete(cweJavaDto);
            responseEntity = new ResponseEntity(cweJavaDto, HttpStatus.OK);
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
        return new ResponseEntity(cweJavaService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(cweJavaService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param cweJavaDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweJava(@RequestBody @Valid CweJavaDto cweJavaDto) {
        long idx = cweJavaService.insertCweJava(cweJavaDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param cweJavaDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweJava(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaDto cweJavaDto) {
        cweJavaService.updateCweJava(idx, cweJavaDto);

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
    public ResponseEntity deleteCweJava(@PathVariable("idx") long idx) throws Exception {
        cweJavaService.deleteRelatedCweJavaByIdx(idx);
        cweJavaAttachedFileService.deleteAllAttachedFile(idx);
        cweJavaCommentService.deleteAllByCweJavaIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param cweJavaIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long cweJavaIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        cweJavaAttachedFileService.uploadAttachedFile(cweJavaIdx, files);

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
        cweJavaAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}