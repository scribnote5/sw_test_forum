package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-c-comments")
public class MisraCCommentController {
    private final MisraCCommentService misraCCommentService;

    public MisraCCommentController(MisraCCommentService misraCCommentService) {
        this.misraCCommentService = misraCCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMisraCComment(@RequestBody @Valid MisraCCommentDto misraCCommentDto) {
        long idx = misraCCommentService.insertMisraCComment(misraCCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCComment(@PathVariable("idx") long idx, @RequestBody @Valid MisraCCommentDto misraCCommentDto) {
        misraCCommentService.updateMisraCComment(idx, misraCCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMisraCComment(@PathVariable("idx") long idx) {
        misraCCommentService.deleteMisraCCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}