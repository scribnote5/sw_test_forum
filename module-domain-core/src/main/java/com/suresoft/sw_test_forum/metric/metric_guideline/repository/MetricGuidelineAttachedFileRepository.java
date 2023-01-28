package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricGuidelineAttachedFileRepository extends JpaRepository<MetricGuidelineAttachedFile, Long> {

}