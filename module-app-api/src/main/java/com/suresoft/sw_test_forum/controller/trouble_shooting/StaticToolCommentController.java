package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.service.StaticToolCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/static-tool-comments")
public class StaticToolCommentController {
    private final StaticToolCommentService staticToolCommentService;

    public StaticToolCommentController(StaticToolCommentService staticToolCommentService) {
        this.staticToolCommentService = staticToolCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param staticToolCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postStaticToolComment(@RequestBody @Valid StaticToolCommentDto staticToolCommentDto) {
        long idx = staticToolCommentService.insertStaticToolComment(staticToolCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param staticToolCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStaticToolComment(@PathVariable("idx") long idx, @RequestBody @Valid StaticToolCommentDto staticToolCommentDto) {
        staticToolCommentService.updateStaticToolComment(idx, staticToolCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStaticToolComment(@PathVariable("idx") long idx) {
        staticToolCommentService.deleteStaticToolCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}