package com.suresoft.sw_test_forum.metric.metric_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MetricGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MetricGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MetricGuidelineComment metricGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuidelineComment.getCreatedDate())
                .lastModifiedDate(metricGuidelineComment.getLastModifiedDate())
                .createdByIdx(metricGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(metricGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuidelineComment.getIdx())
                .auditBoard("메트릭 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(metricGuidelineComment.getMetricGuidelineIdx()))
                .content(metricGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MetricGuidelineComment metricGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuidelineComment.getCreatedDate())
                .lastModifiedDate(metricGuidelineComment.getLastModifiedDate())
                .createdByIdx(metricGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(metricGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuidelineComment.getIdx())
                .auditBoard("메트릭 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(metricGuidelineComment.getMetricGuidelineIdx()))
                .content(metricGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MetricGuidelineComment metricGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(metricGuidelineComment.getCreatedDate())
                .lastModifiedDate(metricGuidelineComment.getLastModifiedDate())
                .createdByIdx(metricGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(metricGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(metricGuidelineComment.getIdx())
                .auditBoard("메트릭 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(metricGuidelineComment.getMetricGuidelineIdx()))
                .content(metricGuidelineComment.getContent())
                .build());
    }
}