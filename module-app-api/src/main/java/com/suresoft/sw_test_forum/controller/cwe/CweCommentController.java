package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.cwe.cwe.dto.CweCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-comments")
public class CweCommentController {
    private final CweCommentService cweCommentService;

    public CweCommentController(CweCommentService cweCommentService) {
        this.cweCommentService = cweCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweComment(@RequestBody @Valid CweCommentDto cweCommentDto) {
        long idx = cweCommentService.insertCweComment(cweCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweComment(@PathVariable("idx") long idx, @RequestBody @Valid CweCommentDto cweCommentDto) {
        cweCommentService.updateCweComment(idx, cweCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweComment(@PathVariable("idx") long idx) {
        cweCommentService.deleteCweCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}