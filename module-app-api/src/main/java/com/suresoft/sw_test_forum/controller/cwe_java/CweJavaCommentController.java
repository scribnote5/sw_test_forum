package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-java-comments")
public class CweJavaCommentController {
    private final CweJavaCommentService cweJavaCommentService;

    public CweJavaCommentController(CweJavaCommentService cweJavaCommentService) {
        this.cweJavaCommentService = cweJavaCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweJavaCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweJavaComment(@RequestBody @Valid CweJavaCommentDto cweJavaCommentDto) {
        long idx = cweJavaCommentService.insertCweJavaComment(cweJavaCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweJavaCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweJavaComment(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaCommentDto cweJavaCommentDto) {
        cweJavaCommentService.updateCweJavaComment(idx, cweJavaCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweJavaComment(@PathVariable("idx") long idx) {
        cweJavaCommentService.deleteCweJavaCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}