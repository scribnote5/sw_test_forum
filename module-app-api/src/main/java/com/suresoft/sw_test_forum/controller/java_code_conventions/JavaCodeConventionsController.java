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
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getJavaCodeConventionsListByPriorityAsc() {
        List<JavaCodeConventionsDto> javaCodeConventionsDtoList = javaCodeConventionsService.findAllByHighPriorityAsc();

        return new ResponseEntity(javaCodeConventionsDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
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
    public ResponseEntity getJavaCodeConventionsWhenRead(@PathVariable("idx") long idx) {
        JavaCodeConventionsDto javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsByIdx(idx);

        javaCodeConventionsDto = javaCodeConventionsAttachedFileService.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsCommentService.findAllByJavaCodeConventionsIdxOrderByIdxDesc(javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleList(idx, javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineList(idx, javaCodeConventionsDto);

        return new ResponseEntity(javaCodeConventionsDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
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
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getJavaCodeConventionsWhenForm(@PathVariable("idx") long idx) {
        JavaCodeConventionsDto javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (javaCodeConventionsDto.isAccess()) {
            javaCodeConventionsDto = javaCodeConventionsAttachedFileService.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsDto);
            javaCodeConventionsDto = javaCodeConventionsService.findJavaCodeConventionsAutoComplete(javaCodeConventionsDto);
            responseEntity = new ResponseEntity(javaCodeConventionsDto, HttpStatus.OK);
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
        return new ResponseEntity(javaCodeConventionsService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(javaCodeConventionsService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
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
     * 수정 페이지에서, 수정
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
     * 삭제 페이지에서, 삭제
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
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param javaCodeConventionsIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long javaCodeConventionsIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        javaCodeConventionsAttachedFileService.uploadAttachedFile(javaCodeConventionsIdx, files);

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
        javaCodeConventionsAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}