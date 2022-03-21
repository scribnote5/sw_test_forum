package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.service.ToolchainCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/toolchain-comments")
public class ToolchainCommentController {
    private final ToolchainCommentService toolchainCommentService;

    public ToolchainCommentController(ToolchainCommentService toolchainCommentService) {
        this.toolchainCommentService = toolchainCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolchainCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postToolchainComment(@RequestBody @Valid ToolchainCommentDto toolchainCommentDto) {
        long idx = toolchainCommentService.insertToolchainComment(toolchainCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolchainCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putToolchainComment(@PathVariable("idx") long idx, @RequestBody @Valid ToolchainCommentDto toolchainCommentDto) {
        toolchainCommentService.updateToolchainComment(idx, toolchainCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteToolchainComment(@PathVariable("idx") long idx) {
        toolchainCommentService.deleteToolchainCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}