package com.suresoft.sw_test_forum.controller.tool.tool_usage_method;

import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodCommentDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.service.ToolUsageMethodCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tool-usage-method-comments")
public class ToolUsageMethodCommentController {
    private final ToolUsageMethodCommentService toolUsageMethodCommentService;

    public ToolUsageMethodCommentController(ToolUsageMethodCommentService toolUsageMethodCommentService) {
        this.toolUsageMethodCommentService = toolUsageMethodCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolUsageMethodCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postToolUsageMethodComment(@RequestBody @Valid ToolUsageMethodCommentDto toolUsageMethodCommentDto) {
        long idx = toolUsageMethodCommentService.insertToolUsageMethodComment(toolUsageMethodCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolUsageMethodCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putToolUsageMethodComment(@PathVariable("idx") long idx, @RequestBody @Valid ToolUsageMethodCommentDto toolUsageMethodCommentDto) {
        toolUsageMethodCommentService.updateToolUsageMethodComment(idx, toolUsageMethodCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteToolUsageMethodComment(@PathVariable("idx") long idx) {
        toolUsageMethodCommentService.deleteToolUsageMethodCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}