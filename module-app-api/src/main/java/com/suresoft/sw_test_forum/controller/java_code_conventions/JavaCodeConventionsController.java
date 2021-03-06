package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsAttachedFileService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsCommentService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineService;
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
@RequestMapping("/api/java-code-conventions")
public class JavaCodeConventionsController {
    private final JavaCodeConventionsService javaCodeConventionsService;
    private final JavaCodeConventionsAttachedFileService javaCodeConventionsAttachedFileService;
    private final JavaCodeConventionsCommentService javaCodeConventionsCommentService;
    private final JavaCodeConventionsExampleService javaCodeConventionsExampleService;
    private final JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService;

    public JavaCodeConventionsController(JavaCodeConventionsService javaCodeConventionsService,
                                         JavaCodeConventionsAttachedFileService javaCodeConventionsAttachedFileService,
                                         JavaCodeConventionsCommentService javaCodeConventionsCommentService,
                                         JavaCodeConventionsExampleService javaCodeConventionsExampleService,
                                         JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService) {
        this.javaCodeConventionsService = javaCodeConventionsService;
        this.javaCodeConventionsAttachedFileService = javaCodeConventionsAttachedFileService;
        this.javaCodeConventionsCommentService = javaCodeConventionsCommentService;
        this.javaCodeConventionsExampleService = javaCodeConventionsExampleService;
        this.javaCodeConventionsGuidelineService = javaCodeConventionsGuidelineService;
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getJavaCodeConventionsListByPriorityAsc() {
        List<JavaCodeConventionsDto> javaCodeConventionsDtoList = javaCodeConventionsService.findAllByHighPriorityAsc();

        return new ResponseEntity(javaCodeConventionsDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param javaCodeConventionsSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getJavaCodeConventionsList(Pageable pageable, JavaCodeConventionsSearchDto javaCodeConventionsSearchDto) {
        Page<JavaCodeConventionsDto> javaCodeConventionsDtoList = javaCodeConventionsService.findAll(pageable, javaCodeConventionsSearchDto);

        return new ResponseEntity(javaCodeConventionsDtoList, HttpStatus.OK);
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
    public ResponseEntity getJavaCodeConventionsWhenRead(@PathVariable("idx") long idx) {
        JavaCodeConventionsDto javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsByIdx(idx);

        javaCodeConventionsDto = javaCodeConventionsAttachedFileService.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsCommentService.findAllByJavaCodeConventionsIdxOrderByIdxDesc(javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleList(idx, javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineList(idx, javaCodeConventionsDto);

        return new ResponseEntity(javaCodeConventionsDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, MISRA C ?????? ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/java-code-conventions-rule/{idx}"})
    public ResponseEntity getJavaCodeConventionsRule(@PathVariable("idx") long idx) {
        String javaCodeConventionsRule = javaCodeConventionsService.findJavaCodeConventionsRuleByIdx(idx);

        return new ResponseEntity(javaCodeConventionsRule, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getJavaCodeConventionsWhenForm(@PathVariable("idx") long idx) {
        JavaCodeConventionsDto javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (javaCodeConventionsDto.isAccess()) {
            javaCodeConventionsDto = javaCodeConventionsAttachedFileService.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsDto);
            javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsAutoComplete(javaCodeConventionsDto);
            responseEntity = new ResponseEntity(javaCodeConventionsDto, HttpStatus.OK);
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
        return new ResponseEntity(javaCodeConventionsService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(javaCodeConventionsService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param javaCodeConventionsDto
     * @return
     */
    @PostMapping
    public ResponseEntity postJavaCodeConventions(@RequestBody @Valid JavaCodeConventionsDto javaCodeConventionsDto) {
        long idx = javaCodeConventionsService.insertJavaCodeConventions(javaCodeConventionsDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param javaCodeConventionsDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventions(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsDto javaCodeConventionsDto) {
        javaCodeConventionsService.updateJavaCodeConventions(idx, javaCodeConventionsDto);

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
    public ResponseEntity deleteJavaCodeConventions(@PathVariable("idx") long idx) throws Exception {
        javaCodeConventionsService.deleteRelatedJavaCodeConventionsByIdx(idx);
        javaCodeConventionsAttachedFileService.deleteAllAttachedFile(idx);
        javaCodeConventionsCommentService.deleteAllByJavaCodeConventionsIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param javaCodeConventionsIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long javaCodeConventionsIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        javaCodeConventionsAttachedFileService.uploadAttachedFile(javaCodeConventionsIdx, files);

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
        javaCodeConventionsAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}