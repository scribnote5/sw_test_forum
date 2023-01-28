package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fx-cop-comments")
public class FxCopCommentController {
    private final FxCopCommentService fxCopCommentService;

    public FxCopCommentController(FxCopCommentService fxCopCommentService) {
        this.fxCopCommentService = fxCopCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param fxCopCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postFxCopComment(@RequestBody @Valid FxCopCommentDto fxCopCommentDto) {
        long idx = fxCopCommentService.insertFxCopComment(fxCopCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param fxCopCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCopComment(@PathVariable("idx") long idx, @RequestBody @Valid FxCopCommentDto fxCopCommentDto) {
        fxCopCommentService.updateFxCopComment(idx, fxCopCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteFxCopComment(@PathVariable("idx") long idx) {
        fxCopCommentService.deleteFxCopCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}