package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fx-cop-guideline-comments")
public class FxCopGuidelineCommentController {
    private final FxCopGuidelineCommentService fxCopGuidelineCommentService;

    public FxCopGuidelineCommentController(FxCopGuidelineCommentService fxCopGuidelineCommentService) {
        this.fxCopGuidelineCommentService = fxCopGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param fxCopGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postFxCopGuidelineComment(@RequestBody @Valid FxCopGuidelineCommentDto fxCopGuidelineCommentDto) {
        long idx = fxCopGuidelineCommentService.insertFxCopGuidelineComment(fxCopGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param fxCopGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCopGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid FxCopGuidelineCommentDto fxCopGuidelineCommentDto) {
        fxCopGuidelineCommentService.updateFxCopGuidelineComment(idx, fxCopGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteFxCopGuidelineComment(@PathVariable("idx") long idx) {
        fxCopGuidelineCommentService.deleteFxCopGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}