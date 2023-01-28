package com.suresoft.sw_test_forum.left_reference.report.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ReportCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ReportCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ReportComment reportComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(reportComment.getCreatedDate())
                .lastModifiedDate(reportComment.getLastModifiedDate())
                .createdByIdx(reportComment.getCreatedByIdx())
                .lastModifiedByIdx(reportComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(reportComment.getIdx())
                .auditBoard("보고서 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(reportComment.getReportIdx()))
                .content(reportComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ReportComment reportComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(reportComment.getCreatedDate())
                .lastModifiedDate(reportComment.getLastModifiedDate())
                .createdByIdx(reportComment.getCreatedByIdx())
                .lastModifiedByIdx(reportComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(reportComment.getIdx())
                .auditBoard("보고서 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(reportComment.getReportIdx()))
                .content(reportComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ReportComment reportComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(reportComment.getCreatedDate())
                .lastModifiedDate(reportComment.getLastModifiedDate())
                .createdByIdx(reportComment.getCreatedByIdx())
                .lastModifiedByIdx(reportComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(reportComment.getIdx())
                .auditBoard("보고서 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(reportComment.getReportIdx()))
                .content(reportComment.getContent())
                .build());
    }
}