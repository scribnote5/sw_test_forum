package com.suresoft.sw_test_forum.controller;

import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notice-comments")
public class NoticeCommentRestController {
    private final NoticeCommentService noticeCommentService;

    public NoticeCommentRestController(NoticeCommentService noticeCommentService) {
        this.noticeCommentService = noticeCommentService;
    }

    @PostMapping
    public ResponseEntity<?> postNoticeComment(@RequestBody @Valid NoticeCommentDto noticeCommentDto) {
        long idx = noticeCommentService.insertNoticeComment(noticeCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> putNoticeComment(@PathVariable("idx") long idx, @RequestBody @Valid NoticeCommentDto moticeCommentDto) {
        noticeCommentService.updateNoticeComment(idx, moticeCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteNoticeComment(@PathVariable("idx") long idx) {
        noticeCommentService.deleteNoticeCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}