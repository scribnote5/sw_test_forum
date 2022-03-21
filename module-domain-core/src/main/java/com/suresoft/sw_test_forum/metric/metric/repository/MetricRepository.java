package com.suresoft.sw_test_forum.metric.metric.repository;

import com.suresoft.sw_test_forum.metric.metric.domain.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

}