package com.suresoft.sw_test_forum.controller.left_reference.report;

import com.suresoft.sw_test_forum.left_reference.report.dto.ReportCommentDto;
import com.suresoft.sw_test_forum.left_reference.report.service.ReportCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/report-comments")
public class ReportCommentController {
    private final ReportCommentService reportCommentService;

    public ReportCommentController(ReportCommentService reportCommentService) {
        this.reportCommentService = reportCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     * @param reportCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postReportComment(@RequestBody @Valid ReportCommentDto reportCommentDto) {
        long idx = reportCommentService.insertReportComment(reportCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     * @param idx
     * @param reportCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putReportComment(@PathVariable("idx") long idx, @RequestBody @Valid ReportCommentDto reportCommentDto) {
        reportCommentService.updateReportComment(idx, reportCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteReportComment(@PathVariable("idx") long idx) {
        reportCommentService.deleteReportCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}