package com.suresoft.sw_test_forum.metric.metric.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricComment metricComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricComment.getCreatedDate())
                .lastModifiedDate(metricComment.getLastModifiedDate())
                .createdByIdx(metricComment.getCreatedByIdx())
                .lastModifiedByIdx(metricComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricComment.getIdx())
                .auditBoard("메트릭 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(metricComment.getMetricIdx()))
                .content(metricComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MetricComment metricComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricComment.getCreatedDate())
                .lastModifiedDate(metricComment.getLastModifiedDate())
                .createdByIdx(metricComment.getCreatedByIdx())
                .lastModifiedByIdx(metricComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricComment.getIdx())
                .auditBoard("메트릭 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(metricComment.getMetricIdx()))
                .content(metricComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MetricComment metricComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricComment.getCreatedDate())
                .lastModifiedDate(metricComment.getLastModifiedDate())
                .createdByIdx(metricComment.getCreatedByIdx())
                .lastModifiedByIdx(metricComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricComment.getIdx())
                .auditBoard("메트릭 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(metricComment.getMetricIdx()))
                .content(metricComment.getContent())
                .build());
    }
}