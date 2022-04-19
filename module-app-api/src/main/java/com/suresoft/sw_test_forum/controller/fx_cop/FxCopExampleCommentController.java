package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fx-cop-example-comments")
public class FxCopExampleCommentController {
    private final FxCopExampleCommentService fxCopExampleCommentService;

    public FxCopExampleCommentController(FxCopExampleCommentService fxCopExampleCommentService) {
        this.fxCopExampleCommentService = fxCopExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param fxCopExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postFxCopExampleComment(@RequestBody @Valid FxCopExampleCommentDto fxCopExampleCommentDto) {
        long idx = fxCopExampleCommentService.insertFxCopExampleComment(fxCopExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param fxCopExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCopExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid FxCopExampleCommentDto fxCopExampleCommentDto) {
        fxCopExampleCommentService.updateFxCopExampleComment(idx, fxCopExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteFxCopExampleComment(@PathVariable("idx") long idx) {
        fxCopExampleCommentService.deleteFxCopExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}