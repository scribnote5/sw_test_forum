package com.suresoft.sw_test_forum.controller.tool.tool_trouble_shooting;

import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingCommentDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service.ToolTroubleShootingCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tool-trouble-shooting-comments")
public class ToolTroubleShootingCommentController {
    private final ToolTroubleShootingCommentService toolTroubleShootingCommentService;

    public ToolTroubleShootingCommentController(ToolTroubleShootingCommentService toolTroubleShootingCommentService) {
        this.toolTroubleShootingCommentService = toolTroubleShootingCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolTroubleShootingCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postTroubleShootingComment(@RequestBody @Valid ToolTroubleShootingCommentDto toolTroubleShootingCommentDto) {
        long idx = toolTroubleShootingCommentService.insertToolTroubleShootingComment(toolTroubleShootingCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolTroubleShootingCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putTroubleShootingComment(@PathVariable("idx") long idx, @RequestBody @Valid ToolTroubleShootingCommentDto toolTroubleShootingCommentDto) {
        toolTroubleShootingCommentService.updateToolTroubleShootingComment(idx, toolTroubleShootingCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteTroubleShootingComment(@PathVariable("idx") long idx) {
        toolTroubleShootingCommentService.deleteTroubleShootingCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}