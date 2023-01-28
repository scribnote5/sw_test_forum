package com.suresoft.sw_test_forum.left_reference.report.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.report.domain.Report;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ReportListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ReportListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Report report) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(report.getCreatedDate())
                .lastModifiedDate(report.getLastModifiedDate())
                .createdByIdx(report.getCreatedByIdx())
                .lastModifiedByIdx(report.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(report.getIdx())
                .auditBoard("보고서")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(report.getTitle()))
                .content(report.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Report report) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(report.getCreatedDate())
                .lastModifiedDate(report.getLastModifiedDate())
                .createdByIdx(report.getCreatedByIdx())
                .lastModifiedByIdx(report.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(report.getIdx())
                .auditBoard("보고서")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(report.getTitle()))
                .content(report.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Report report) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(report.getCreatedDate())
                .lastModifiedDate(report.getLastModifiedDate())
                .createdByIdx(report.getCreatedByIdx())
                .lastModifiedByIdx(report.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(report.getIdx())
                .auditBoard("보고서")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(report.getTitle()))
                .content(report.getContent())
                .build());
    }
    
}
