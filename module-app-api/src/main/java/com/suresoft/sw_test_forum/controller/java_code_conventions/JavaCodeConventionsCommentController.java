package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/java-code-conventions-comments")
public class JavaCodeConventionsCommentController {
    private final JavaCodeConventionsCommentService javaCodeConventionsCommentService;

    public JavaCodeConventionsCommentController(JavaCodeConventionsCommentService javaCodeConventionsCommentService) {
        this.javaCodeConventionsCommentService = javaCodeConventionsCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param javaCodeConventionsCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postJavaCodeConventionsComment(@RequestBody @Valid JavaCodeConventionsCommentDto javaCodeConventionsCommentDto) {
        long idx = javaCodeConventionsCommentService.insertJavaCodeConventionsComment(javaCodeConventionsCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param javaCodeConventionsCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventionsComment(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsCommentDto javaCodeConventionsCommentDto) {
        javaCodeConventionsCommentService.updateJavaCodeConventionsComment(idx, javaCodeConventionsCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteJavaCodeConventionsComment(@PathVariable("idx") long idx) {
        javaCodeConventionsCommentService.deleteJavaCodeConventionsCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}