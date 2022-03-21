package com.suresoft.sw_test_forum.metric.metric_example.repository;

import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricExampleCommentRepository extends JpaRepository<MetricExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param metricExampleIdx
     * @return
     */
    List<MetricExampleComment> findAllByMetricExampleIdxOrderByIdxDesc(long metricExampleIdx);
}