package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/style-cop-example-comments")
public class StyleCopExampleCommentController {
    private final StyleCopExampleCommentService styleCopExampleCommentService;

    public StyleCopExampleCommentController(StyleCopExampleCommentService styleCopExampleCommentService) {
        this.styleCopExampleCommentService = styleCopExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postStyleCopExampleComment(@RequestBody @Valid StyleCopExampleCommentDto styleCopExampleCommentDto) {
        long idx = styleCopExampleCommentService.insertStyleCopExampleComment(styleCopExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param styleCopExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCopExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopExampleCommentDto styleCopExampleCommentDto) {
        styleCopExampleCommentService.updateStyleCopExampleComment(idx, styleCopExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStyleCopExampleComment(@PathVariable("idx") long idx) {
        styleCopExampleCommentService.deleteStyleCopExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}