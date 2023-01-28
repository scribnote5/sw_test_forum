package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleCommentService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/java-code-conventions-examples")
public class JavaCodeConventionsExampleController {
    private final JavaCodeConventionsExampleService javaCodeConventionsExampleService;
    private final JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService;

    public JavaCodeConventionsExampleController(JavaCodeConventionsExampleService javaCodeConventionsExampleService,
                                                JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService) {
        this.javaCodeConventionsExampleService = javaCodeConventionsExampleService;
        this.javaCodeConventionsExampleCommentService = javaCodeConventionsExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getJavaCodeConventionsExampleList(Pageable pageable, JavaCodeConventionsExampleSearchDto javaCodeConventionsExampleSearchDto) {
        Page<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList = javaCodeConventionsExampleService.findJavaCodeConventionsExampleList(pageable, javaCodeConventionsExampleSearchDto);

        return new ResponseEntity(javaCodeConventionsExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getJavaCodeConventionsExampleWhenRead(@PathVariable("idx") long idx) {
        JavaCodeConventionsExampleDto javaCodeConventionsExampleDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleByIdx(idx);

        javaCodeConventionsExampleDto = javaCodeConventionsExampleCommentService.findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(javaCodeConventionsExampleDto);

        return new ResponseEntity(javaCodeConventionsExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getJavaCodeConventionsExampleWhenForm(@PathVariable("idx") long idx) {
        JavaCodeConventionsExampleDto javaCodeConventionsExampleDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (javaCodeConventionsExampleDto.isAccess()) {
            javaCodeConventionsExampleDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleAutoComplete(javaCodeConventionsExampleDto);
            responseEntity = new ResponseEntity(javaCodeConventionsExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{javaCodeConventionsIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("javaCodeConventionsIdx") long javaCodeConventionsIdx) {
        return new ResponseEntity(javaCodeConventionsExampleService.findPriorityListByHighPriorityAscWhenWrite(javaCodeConventionsIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{javaCodeConventionsIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("javaCodeConventionsIdx") long javaCodeConventionsIdx) {
        return new ResponseEntity(javaCodeConventionsExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, javaCodeConventionsIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param javaCodeConventionsExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postJavaCodeConventionsExample(@RequestBody @Valid JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        long idx = javaCodeConventionsExampleService.insertJavaCodeConventionsExample(javaCodeConventionsExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param javaCodeConventionsExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventionsExample(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        javaCodeConventionsExampleService.updateJavaCodeConventionsExample(idx, javaCodeConventionsExampleDto);

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
    public ResponseEntity deleteJavaCodeConventionsExample(@PathVariable("idx") long idx) throws Exception {
        javaCodeConventionsExampleService.deleteJavaCodeConventionsExampleByIdx(idx);
        javaCodeConventionsExampleCommentService.deleteAllByJavaCodeConventionsExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}