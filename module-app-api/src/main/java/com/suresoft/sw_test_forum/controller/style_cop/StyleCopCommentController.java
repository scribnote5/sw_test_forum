package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/style-cop-comments")
public class StyleCopCommentController {
    private final StyleCopCommentService styleCopCommentService;

    public StyleCopCommentController(StyleCopCommentService styleCopCommentService) {
        this.styleCopCommentService = styleCopCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postStyleCopComment(@RequestBody @Valid StyleCopCommentDto styleCopCommentDto) {
        long idx = styleCopCommentService.insertStyleCopComment(styleCopCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param styleCopCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCopComment(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopCommentDto styleCopCommentDto) {
        styleCopCommentService.updateStyleCopComment(idx, styleCopCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStyleCopComment(@PathVariable("idx") long idx) {
        styleCopCommentService.deleteStyleCopCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}