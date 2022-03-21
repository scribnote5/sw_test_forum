package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.metric.metric.dto.MetricCommentDto;
import com.suresoft.sw_test_forum.metric.metric.service.MetricCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/metric-comments")
public class MetricCommentController {
    private final MetricCommentService metricCommentService;

    public MetricCommentController(MetricCommentService metricCommentService) {
        this.metricCommentService = metricCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param metricCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMetricComment(@RequestBody @Valid MetricCommentDto metricCommentDto) {
        long idx = metricCommentService.insertMetricComment(metricCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param metricCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetricComment(@PathVariable("idx") long idx, @RequestBody @Valid MetricCommentDto metricCommentDto) {
        metricCommentService.updateMetricComment(idx, metricCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMetricComment(@PathVariable("idx") long idx) {
        metricCommentService.deleteMetricCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}