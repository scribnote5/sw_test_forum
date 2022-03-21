package com.suresoft.sw_test_forum.metric.metric.repository;

import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricCommentRepository extends JpaRepository<MetricComment, Long> {
    /**
     * 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    List<MetricComment> findAllByMetricIdxOrderByIdxDesc(long metricIdx);
}