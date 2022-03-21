package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-c-guideline-comments")
public class MisraCGuidelineCommentController {
    private final MisraCGuidelineCommentService misraCGuidelineCommentService;

    public MisraCGuidelineCommentController(MisraCGuidelineCommentService misraCGuidelineCommentService) {
        this.misraCGuidelineCommentService = misraCGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCGuidelineComment(@RequestBody @Valid MisraCGuidelineCommentDto misraCGuidelineCommentDto) {
        long idx = misraCGuidelineCommentService.insertMisraCGuidelineComment(misraCGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCGuidelineCommentDto misraCGuidelineCommentDto) {
        misraCGuidelineCommentService.updateMisraCGuidelineComment(idx, misraCGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCGuidelineComment(@PathVariable("idx") long idx) {
        misraCGuidelineCommentService.deleteMisraCGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}