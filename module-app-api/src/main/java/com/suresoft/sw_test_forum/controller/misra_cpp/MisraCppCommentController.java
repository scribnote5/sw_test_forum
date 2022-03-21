package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-cpp-comments")
public class MisraCppCommentController {
    private final MisraCppCommentService misraCppCommentService;

    public MisraCppCommentController(MisraCppCommentService misraCppCommentService) {
        this.misraCppCommentService = misraCppCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCppCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCppComment(@RequestBody @Valid MisraCppCommentDto misraCppCommentDto) {
        long idx = misraCppCommentService.insertMisraCppComment(misraCppCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCppCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCppComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppCommentDto misraCppCommentDto) {
        misraCppCommentService.updateMisraCppComment(idx, misraCppCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCppComment(@PathVariable("idx") long idx) {
        misraCppCommentService.deleteMisraCppCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}