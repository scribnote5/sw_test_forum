package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-java-example-comments")
public class CweJavaExampleCommentController {
    private final CweJavaExampleCommentService cweJavaExampleCommentService;

    public CweJavaExampleCommentController(CweJavaExampleCommentService cweJavaExampleCommentService) {
        this.cweJavaExampleCommentService = cweJavaExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweJavaExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweJavaExampleComment(@RequestBody @Valid CweJavaExampleCommentDto cweJavaExampleCommentDto) {
        long idx = cweJavaExampleCommentService.insertCweJavaExampleComment(cweJavaExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param cweJavaExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweJavaExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaExampleCommentDto cweJavaExampleCommentDto) {
        cweJavaExampleCommentService.updateCweJavaExampleComment(idx, cweJavaExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweJavaExampleComment(@PathVariable("idx") long idx) {
        cweJavaExampleCommentService.deleteCweJavaExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}