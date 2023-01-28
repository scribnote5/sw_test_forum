package com.suresoft.sw_test_forum.metric.metric_example.repository;

import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricExampleRepository extends JpaRepository<MetricExample, Long> {
    public void deleteAllByMetricIdx(long metricIdx);
}