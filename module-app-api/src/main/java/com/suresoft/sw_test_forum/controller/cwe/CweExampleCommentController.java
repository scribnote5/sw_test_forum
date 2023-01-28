package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-example-comments")
public class CweExampleCommentController {
    private final CweExampleCommentService cweExampleCommentService;

    public CweExampleCommentController(CweExampleCommentService cweExampleCommentService) {
        this.cweExampleCommentService = cweExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweExampleComment(@RequestBody @Valid CweExampleCommentDto cweExampleCommentDto) {
        long idx = cweExampleCommentService.insertCweExampleComment(cweExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param cweExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid CweExampleCommentDto cweExampleCommentDto) {
        cweExampleCommentService.updateCweExampleComment(idx, cweExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweExampleComment(@PathVariable("idx") long idx) {
        cweExampleCommentService.deleteCweExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}