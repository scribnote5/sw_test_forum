package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-java-guideline-comments")
public class CweJavaGuidelineCommentController {
    private final CweJavaGuidelineCommentService cweJavaGuidelineCommentService;

    public CweJavaGuidelineCommentController(CweJavaGuidelineCommentService cweJavaGuidelineCommentService) {
        this.cweJavaGuidelineCommentService = cweJavaGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweJavaGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCweJavaGuidelineComment(@RequestBody @Valid CweJavaGuidelineCommentDto cweJavaGuidelineCommentDto) {
        long idx = cweJavaGuidelineCommentService.insertCweJavaGuidelineComment(cweJavaGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweJavaGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweJavaGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaGuidelineCommentDto cweJavaGuidelineCommentDto) {
        cweJavaGuidelineCommentService.updateCweJavaGuidelineComment(idx, cweJavaGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCweJavaGuidelineComment(@PathVariable("idx") long idx) {
        cweJavaGuidelineCommentService.deleteCweJavaGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}