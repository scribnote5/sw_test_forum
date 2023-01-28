package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notice-comments")
public class NoticeCommentController {
    private final NoticeCommentService noticeCommentService;

    public NoticeCommentController(NoticeCommentService noticeCommentService) {
        this.noticeCommentService = noticeCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     * @param noticeCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postNoticeComment(@RequestBody @Valid NoticeCommentDto noticeCommentDto) {
        long idx = noticeCommentService.insertNoticeComment(noticeCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     * @param idx
     * @param noticeCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putNoticeComment(@PathVariable("idx") long idx, @RequestBody @Valid NoticeCommentDto noticeCommentDto) {
        noticeCommentService.updateNoticeComment(idx, noticeCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteNoticeComment(@PathVariable("idx") long idx) {
        noticeCommentService.deleteNoticeCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}