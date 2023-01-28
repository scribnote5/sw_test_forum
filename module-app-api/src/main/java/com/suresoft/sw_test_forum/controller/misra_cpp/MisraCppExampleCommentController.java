package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-cpp-example-comments")
public class MisraCppExampleCommentController {
    private final MisraCppExampleCommentService misraCppExampleCommentService;

    public MisraCppExampleCommentController(MisraCppExampleCommentService misraCppExampleCommentService) {
        this.misraCppExampleCommentService = misraCppExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCppExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCppExampleComment(@RequestBody @Valid MisraCppExampleCommentDto misraCppExampleCommentDto) {
        long idx = misraCppExampleCommentService.insertMisraCppExampleComment(misraCppExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param misraCppExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCppExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppExampleCommentDto misraCppExampleCommentDto) {
        misraCppExampleCommentService.updateMisraCppExampleComment(idx, misraCppExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCppExampleComment(@PathVariable("idx") long idx) {
        misraCppExampleCommentService.deleteMisraCppExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}