package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-c-example-comments")
public class MisraCExampleCommentController {
    private final MisraCExampleCommentService misraCExampleCommentService;

    public MisraCExampleCommentController(MisraCExampleCommentService misraCExampleCommentService) {
        this.misraCExampleCommentService = misraCExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCExampleComment(@RequestBody @Valid MisraCExampleCommentDto misraCExampleCommentDto) {
        long idx = misraCExampleCommentService.insertMisraCExampleComment(misraCExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param misraCExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCExampleCommentDto misraCExampleCommentDto) {
        misraCExampleCommentService.updateMisraCExampleComment(idx, misraCExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCExampleComment(@PathVariable("idx") long idx) {
        misraCExampleCommentService.deleteMisraCExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}