package com.suresoft.sw_test_forum.metric.metric_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class MetricGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricGuidelineLike metricGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuidelineLike.getCreatedDate())
                .lastModifiedDate(metricGuidelineLike.getLastModifiedDate())
                .createdByIdx(metricGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(metricGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuidelineLike.getIdx())
                .auditBoard("메트릭 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(metricGuidelineLike.getMetricGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(MetricGuidelineLike metricGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuidelineLike.getCreatedDate())
                .lastModifiedDate(metricGuidelineLike.getLastModifiedDate())
                .createdByIdx(metricGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(metricGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuidelineLike.getIdx())
                .auditBoard("메트릭 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(metricGuidelineLike.getMetricGuidelineIdx()))
                .build());
    }
}