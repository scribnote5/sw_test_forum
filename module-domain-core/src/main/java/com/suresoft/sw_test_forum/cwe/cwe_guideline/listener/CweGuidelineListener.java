package com.suresoft.sw_test_forum.cwe.cwe_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweGuideline cweGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuideline.getCreatedDate())
                .lastModifiedDate(cweGuideline.getLastModifiedDate())
                .createdByIdx(cweGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuideline.getIdx())
                .auditBoard("CWE C/C++ 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cweGuideline.getTitle()))
                .content(cweGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweGuideline cweGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuideline.getCreatedDate())
                .lastModifiedDate(cweGuideline.getLastModifiedDate())
                .createdByIdx(cweGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuideline.getIdx())
                .auditBoard("CWE C/C++ 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cweGuideline.getTitle()))
                .content(cweGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweGuideline cweGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuideline.getCreatedDate())
                .lastModifiedDate(cweGuideline.getLastModifiedDate())
                .createdByIdx(cweGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuideline.getIdx())
                .auditBoard("CWE C/C++ 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cweGuideline.getTitle()))
                .content(cweGuideline.getContent())
                .build());
    }
}