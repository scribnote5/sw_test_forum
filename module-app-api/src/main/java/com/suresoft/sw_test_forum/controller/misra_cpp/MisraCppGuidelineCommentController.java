package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-cpp-guideline-comments")
public class MisraCppGuidelineCommentController {
    private final MisraCppGuidelineCommentService misraCppGuidelineCommentService;

    public MisraCppGuidelineCommentController(MisraCppGuidelineCommentService misraCppGuidelineCommentService) {
        this.misraCppGuidelineCommentService = misraCppGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCppGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCppGuidelineComment(@RequestBody @Valid MisraCppGuidelineCommentDto misraCppGuidelineCommentDto) {
        long idx = misraCppGuidelineCommentService.insertMisraCppGuidelineComment(misraCppGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCppGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCppGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppGuidelineCommentDto misraCppGuidelineCommentDto) {
        misraCppGuidelineCommentService.updateMisraCppGuidelineComment(idx, misraCppGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCppGuidelineComment(@PathVariable("idx") long idx) {
        misraCppGuidelineCommentService.deleteMisraCppGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}