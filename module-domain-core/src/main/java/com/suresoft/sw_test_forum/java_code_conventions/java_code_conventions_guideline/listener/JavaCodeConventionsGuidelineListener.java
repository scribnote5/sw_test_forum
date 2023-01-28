package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsGuideline javaCodeConventionsGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuideline.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuideline.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuideline.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuideline.getIdx())
                .auditBoard("Java Code Conventions 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(javaCodeConventionsGuideline.getTitle()))
                .content(javaCodeConventionsGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventionsGuideline javaCodeConventionsGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuideline.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuideline.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuideline.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuideline.getIdx())
                .auditBoard("Java Code Conventions 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(javaCodeConventionsGuideline.getTitle()))
                .content(javaCodeConventionsGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsGuideline javaCodeConventionsGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuideline.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuideline.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuideline.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuideline.getIdx())
                .auditBoard("Java Code Conventions 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(javaCodeConventionsGuideline.getTitle()))
                .content(javaCodeConventionsGuideline.getContent())
                .build());
    }
}