package com.suresoft.sw_test_forum.metric.metric_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricExample metricExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExample.getCreatedDate())
                .lastModifiedDate(metricExample.getLastModifiedDate())
                .createdByIdx(metricExample.getCreatedByIdx())
                .lastModifiedByIdx(metricExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExample.getIdx())
                .auditBoard("메트릭 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(metricExample.getTitle()))
                .content(metricExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MetricExample metricExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExample.getCreatedDate())
                .lastModifiedDate(metricExample.getLastModifiedDate())
                .createdByIdx(metricExample.getCreatedByIdx())
                .lastModifiedByIdx(metricExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExample.getIdx())
                .auditBoard("메트릭 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(metricExample.getTitle()))
                .content(metricExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MetricExample metricExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExample.getCreatedDate())
                .lastModifiedDate(metricExample.getLastModifiedDate())
                .createdByIdx(metricExample.getCreatedByIdx())
                .lastModifiedByIdx(metricExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExample.getIdx())
                .auditBoard("메트릭 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(metricExample.getTitle()))
                .content(metricExample.getContent())
                .build());
    }

}
