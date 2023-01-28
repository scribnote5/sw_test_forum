package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsExample javaCodeConventionsExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExample.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExample.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExample.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExample.getIdx())
                .auditBoard("Java Code Conventions 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(javaCodeConventionsExample.getTitle()))
                .content(javaCodeConventionsExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventionsExample javaCodeConventionsExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExample.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExample.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExample.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExample.getIdx())
                .auditBoard("Java Code Conventions 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(javaCodeConventionsExample.getTitle()))
                .content(javaCodeConventionsExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsExample javaCodeConventionsExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExample.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExample.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExample.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExample.getIdx())
                .auditBoard("Java Code Conventions 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(javaCodeConventionsExample.getTitle()))
                .content(javaCodeConventionsExample.getContent())
                .build());
    }
}
