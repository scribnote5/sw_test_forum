package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/java-code-conventions-example-comments")
public class JavaCodeConventionsExampleCommentController {
    private final JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService;

    public JavaCodeConventionsExampleCommentController(JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService) {
        this.javaCodeConventionsExampleCommentService = javaCodeConventionsExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param javaCodeConventionsExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postJavaCodeConventionsExampleComment(@RequestBody @Valid JavaCodeConventionsExampleCommentDto javaCodeConventionsExampleCommentDto) {
        long idx = javaCodeConventionsExampleCommentService.insertJavaCodeConventionsExampleComment(javaCodeConventionsExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param javaCodeConventionsExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventionsExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsExampleCommentDto javaCodeConventionsExampleCommentDto) {
        javaCodeConventionsExampleCommentService.updateJavaCodeConventionsExampleComment(idx, javaCodeConventionsExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteJavaCodeConventionsExampleComment(@PathVariable("idx") long idx) {
        javaCodeConventionsExampleCommentService.deleteJavaCodeConventionsExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}