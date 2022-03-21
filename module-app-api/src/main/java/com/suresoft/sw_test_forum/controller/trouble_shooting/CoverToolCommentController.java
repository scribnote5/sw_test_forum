package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service.CoverToolCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cover-tool-comments")
public class CoverToolCommentController {
    private final CoverToolCommentService coverToolCommentService;

    public CoverToolCommentController(CoverToolCommentService coverToolCommentService) {
        this.coverToolCommentService = coverToolCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param coverToolCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postCoverToolComment(@RequestBody @Valid CoverToolCommentDto coverToolCommentDto) {
        long idx = coverToolCommentService.insertCoverToolComment(coverToolCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param coverToolCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCoverToolComment(@PathVariable("idx") long idx, @RequestBody @Valid CoverToolCommentDto coverToolCommentDto) {
        coverToolCommentService.updateCoverToolComment(idx, coverToolCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteCoverToolComment(@PathVariable("idx") long idx) {
        coverToolCommentService.deleteCoverToolCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}