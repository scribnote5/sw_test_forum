package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metric-guideline-likes")
public class MetricGuidelineLikeController {
    private final MetricGuidelineLikeService metricGuidelineLikeService;

    public MetricGuidelineLikeController(MetricGuidelineLikeService metricGuidelineLikeService) {
        this.metricGuidelineLikeService = metricGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param metricGuidelineIdx
     * @return
     */
    @PostMapping("/{metricGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("metricGuidelineIdx") long metricGuidelineIdx) {
        long idx = metricGuidelineLikeService.insertMetricGuidelineLike(metricGuidelineIdx);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 읽기 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteLike(@PathVariable("idx") long idx) throws Exception {
        metricGuidelineLikeService.deleteMetricGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}