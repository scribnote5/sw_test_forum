package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleCommentDto;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/metric-example-comments")
public class MetricExampleCommentController {
    private final MetricExampleCommentService metricExampleCommentService;

    public MetricExampleCommentController(MetricExampleCommentService metricExampleCommentService) {
        this.metricExampleCommentService = metricExampleCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param metricExampleCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMetricExampleComment(@RequestBody @Valid MetricExampleCommentDto metricExampleCommentDto) {
        long idx = metricExampleCommentService.insertMetricExampleComment(metricExampleCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 등록
     *
     * @param idx
     * @param metricExampleCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetricExampleComment(@PathVariable("idx") long idx, @RequestBody @Valid MetricExampleCommentDto metricExampleCommentDto) {
        metricExampleCommentService.updateMetricExampleComment(idx, metricExampleCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMetricExampleComment(@PathVariable("idx") long idx) {
        metricExampleCommentService.deleteMetricExampleCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}