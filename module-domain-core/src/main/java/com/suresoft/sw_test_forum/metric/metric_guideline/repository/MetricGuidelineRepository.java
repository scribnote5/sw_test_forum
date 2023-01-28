package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricGuidelineRepository extends JpaRepository<MetricGuideline, Long> {
    public void deleteAllByMetricIdx(long metricIdx);
}