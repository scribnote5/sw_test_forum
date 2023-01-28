package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricGuidelineLikeRepository extends JpaRepository<MetricGuidelineLike, Long> {
    /**
     * metricGuideline 좋아요 갯수 조회
     *
     * @param metricGuidelineIdx
     * @return
     */
    public long countByMetricGuidelineIdx(long metricGuidelineIdx);
}