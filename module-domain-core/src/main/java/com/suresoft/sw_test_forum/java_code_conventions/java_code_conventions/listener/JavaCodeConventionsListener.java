package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventions;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventions javaCodeConventions) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventions.getCreatedDate())
                .lastModifiedDate(javaCodeConventions.getLastModifiedDate())
                .createdByIdx(javaCodeConventions.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventions.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventions.getIdx())
                .auditBoard("Java Code Conventions 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(javaCodeConventions.getTitle()))
                .content(javaCodeConventions.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventions javaCodeConventions) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventions.getCreatedDate())
                .lastModifiedDate(javaCodeConventions.getLastModifiedDate())
                .createdByIdx(javaCodeConventions.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventions.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventions.getIdx())
                .auditBoard("Java Code Conventions 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(javaCodeConventions.getTitle()))
                .content(javaCodeConventions.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventions javaCodeConventions) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventions.getCreatedDate())
                .lastModifiedDate(javaCodeConventions.getLastModifiedDate())
                .createdByIdx(javaCodeConventions.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventions.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventions.getIdx())
                .auditBoard("Java Code Conventions 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(javaCodeConventions.getTitle()))
                .content(javaCodeConventions.getContent())
                .build());
    }
}