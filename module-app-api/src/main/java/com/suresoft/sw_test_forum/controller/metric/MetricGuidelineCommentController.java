package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineCommentDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/metric-guideline-comments")
public class MetricGuidelineCommentController {
    private final MetricGuidelineCommentService metricGuidelineCommentService;

    public MetricGuidelineCommentController(MetricGuidelineCommentService metricGuidelineCommentService) {
        this.metricGuidelineCommentService = metricGuidelineCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param metricGuidelineCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postMetricGuidelineComment(@RequestBody @Valid MetricGuidelineCommentDto metricGuidelineCommentDto) {
        long idx = metricGuidelineCommentService.insertMetricGuidelineComment(metricGuidelineCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param metricGuidelineCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetricGuidelineComment(@PathVariable("idx") long idx, @RequestBody @Valid MetricGuidelineCommentDto metricGuidelineCommentDto) {
        metricGuidelineCommentService.updateMetricGuidelineComment(idx, metricGuidelineCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteMetricGuidelineComment(@PathVariable("idx") long idx) {
        metricGuidelineCommentService.deleteMetricGuidelineCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}