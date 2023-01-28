package com.suresoft.sw_test_forum.metric.metric.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric.domain.Metric;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Metric metric) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metric.getCreatedDate())
                .lastModifiedDate(metric.getLastModifiedDate())
                .createdByIdx(metric.getCreatedByIdx())
                .lastModifiedByIdx(metric.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metric.getIdx())
                .auditBoard("메트릭 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(metric.getTitle()))
                .content(metric.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Metric metric) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metric.getCreatedDate())
                .lastModifiedDate(metric.getLastModifiedDate())
                .createdByIdx(metric.getCreatedByIdx())
                .lastModifiedByIdx(metric.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metric.getIdx())
                .auditBoard("메트릭 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(metric.getTitle()))
                .content(metric.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Metric metric) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metric.getCreatedDate())
                .lastModifiedDate(metric.getLastModifiedDate())
                .createdByIdx(metric.getCreatedByIdx())
                .lastModifiedByIdx(metric.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metric.getIdx())
                .auditBoard("메트릭")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(metric.getTitle()))
                .content(metric.getContent())
                .build());
    }
}