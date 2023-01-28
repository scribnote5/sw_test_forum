package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/style-cop-guideline-comments")
public class StyleCopGuidelineCommentController {
    private final StyleCopGuidelineCommentService styleCopGuidelineCommentService;

    public StyleCopGuidelineCommentController(StyleCopGuidelineCommentService styleCopGuidelineCommentService) {
        this.styleCopGuidelineCommentService = styleCopGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postStyleCopGuidelineComment(@RequestBody @Valid StyleCopGuidelineCommentDto styleCopGuidelineCommentDto) {
        long idx = styleCopGuidelineCommentService.insertStyleCopGuidelineComment(styleCopGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param styleCopGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCopGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopGuidelineCommentDto styleCopGuidelineCommentDto) {
        styleCopGuidelineCommentService.updateStyleCopGuidelineComment(idx, styleCopGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStyleCopGuidelineComment(@PathVariable("idx") long idx) {
        styleCopGuidelineCommentService.deleteStyleCopGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}