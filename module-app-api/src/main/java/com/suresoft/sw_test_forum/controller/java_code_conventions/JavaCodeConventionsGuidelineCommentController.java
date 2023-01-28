package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/java-code-conventions-guideline-comments")
public class JavaCodeConventionsGuidelineCommentController {
    private final JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService;

    public JavaCodeConventionsGuidelineCommentController(JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService) {
        this.javaCodeConventionsGuidelineCommentService = javaCodeConventionsGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param javaCodeConventionsGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postJavaCodeConventionsGuidelineComment(@RequestBody @Valid JavaCodeConventionsGuidelineCommentDto javaCodeConventionsGuidelineCommentDto) {
        long idx = javaCodeConventionsGuidelineCommentService.insertJavaCodeConventionsGuidelineComment(javaCodeConventionsGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param javaCodeConventionsGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventionsGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsGuidelineCommentDto javaCodeConventionsGuidelineCommentDto) {
        javaCodeConventionsGuidelineCommentService.updateJavaCodeConventionsGuidelineComment(idx, javaCodeConventionsGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteJavaCodeConventionsGuidelineComment(@PathVariable("idx") long idx) {
        javaCodeConventionsGuidelineCommentService.deleteJavaCodeConventionsGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}