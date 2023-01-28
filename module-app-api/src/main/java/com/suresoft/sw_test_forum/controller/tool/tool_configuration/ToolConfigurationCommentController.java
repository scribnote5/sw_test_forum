package com.suresoft.sw_test_forum.controller.tool.tool_configuration;

import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationCommentDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.service.ToolConfigurationCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tool-configuration-comments")
public class ToolConfigurationCommentController {
    private final ToolConfigurationCommentService toolConfigurationCommentService;

    public ToolConfigurationCommentController(ToolConfigurationCommentService toolConfigurationCommentService) {
        this.toolConfigurationCommentService = toolConfigurationCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolConfigurationCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postConfigurationComment(@RequestBody @Valid ToolConfigurationCommentDto toolConfigurationCommentDto) {
        long idx = toolConfigurationCommentService.insertConfigurationComment(toolConfigurationCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolConfigurationCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putConfigurationComment(@PathVariable("idx") long idx, @RequestBody @Valid ToolConfigurationCommentDto toolConfigurationCommentDto) {
        toolConfigurationCommentService.updateConfigurationComment(idx, toolConfigurationCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteConfigurationComment(@PathVariable("idx") long idx) {
        toolConfigurationCommentService.deleteConfigurationCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}