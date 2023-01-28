package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricGuidelineCommentRepository extends JpaRepository<MetricGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param metricGuidelineIdx
     * @return
     */
    List<MetricGuidelineComment> findAllByMetricGuidelineIdxOrderByIdxDesc(long metricGuidelineIdx);
}