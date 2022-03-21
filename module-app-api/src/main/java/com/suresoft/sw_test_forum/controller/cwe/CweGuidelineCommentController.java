package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-guideline-comments")
public class CweGuidelineCommentController {
    private final CweGuidelineCommentService cweGuidelineCommentService;

    public CweGuidelineCommentController(CweGuidelineCommentService cweGuidelineCommentService) {
        this.cweGuidelineCommentService = cweGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweGuidelineComment(@RequestBody @Valid CweGuidelineCommentDto cweGuidelineCommentDto) {
        long idx = cweGuidelineCommentService.insertCweGuidelineComment(cweGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid CweGuidelineCommentDto cweGuidelineCommentDto) {
        cweGuidelineCommentService.updateCweGuidelineComment(idx, cweGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweGuidelineComment(@PathVariable("idx") long idx) {
        cweGuidelineCommentService.deleteCweGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}