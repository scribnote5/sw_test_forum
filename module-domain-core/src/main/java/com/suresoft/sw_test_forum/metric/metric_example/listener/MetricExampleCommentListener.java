package com.suresoft.sw_test_forum.metric.metric_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricExampleComment metricExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExampleComment.getCreatedDate())
                .lastModifiedDate(metricExampleComment.getLastModifiedDate())
                .createdByIdx(metricExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(metricExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExampleComment.getIdx())
                .auditBoard("메트릭 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(metricExampleComment.getMetricExampleIdx()))
                .content(metricExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MetricExampleComment metricExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExampleComment.getCreatedDate())
                .lastModifiedDate(metricExampleComment.getLastModifiedDate())
                .createdByIdx(metricExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(metricExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExampleComment.getIdx())
                .auditBoard("메트릭 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(metricExampleComment.getMetricExampleIdx()))
                .content(metricExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MetricExampleComment metricExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricExampleComment.getCreatedDate())
                .lastModifiedDate(metricExampleComment.getLastModifiedDate())
                .createdByIdx(metricExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(metricExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricExampleComment.getIdx())
                .auditBoard("메트릭 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(metricExampleComment.getMetricExampleIdx()))
                .content(metricExampleComment.getContent())
                .build());
    }
}