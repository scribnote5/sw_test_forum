package com.suresoft.sw_test_forum.metric.metric_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricGuideline metricGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuideline.getCreatedDate())
                .lastModifiedDate(metricGuideline.getLastModifiedDate())
                .createdByIdx(metricGuideline.getCreatedByIdx())
                .lastModifiedByIdx(metricGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuideline.getIdx())
                .auditBoard("메트릭 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(metricGuideline.getTitle()))
                .content(metricGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MetricGuideline metricGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuideline.getCreatedDate())
                .lastModifiedDate(metricGuideline.getLastModifiedDate())
                .createdByIdx(metricGuideline.getCreatedByIdx())
                .lastModifiedByIdx(metricGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuideline.getIdx())
                .auditBoard("메트릭 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(metricGuideline.getTitle()))
                .content(metricGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MetricGuideline metricGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuideline.getCreatedDate())
                .lastModifiedDate(metricGuideline.getLastModifiedDate())
                .createdByIdx(metricGuideline.getCreatedByIdx())
                .lastModifiedByIdx(metricGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuideline.getIdx())
                .auditBoard("메트릭 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(metricGuideline.getTitle()))
                .content(metricGuideline.getContent())
                .build());
    }
}