package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaGuideline cweJavaGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuideline.getCreatedDate())
                .lastModifiedDate(cweJavaGuideline.getLastModifiedDate())
                .createdByIdx(cweJavaGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuideline.getIdx())
                .auditBoard("CWE Java 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cweJavaGuideline.getTitle()))
                .content(cweJavaGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJavaGuideline cweJavaGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuideline.getCreatedDate())
                .lastModifiedDate(cweJavaGuideline.getLastModifiedDate())
                .createdByIdx(cweJavaGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuideline.getIdx())
                .auditBoard("CWE Java 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cweJavaGuideline.getTitle()))
                .content(cweJavaGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaGuideline cweJavaGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuideline.getCreatedDate())
                .lastModifiedDate(cweJavaGuideline.getLastModifiedDate())
                .createdByIdx(cweJavaGuideline.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuideline.getIdx())
                .auditBoard("CWE Java 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cweJavaGuideline.getTitle()))
                .content(cweJavaGuideline.getContent())
                .build());
    }
}