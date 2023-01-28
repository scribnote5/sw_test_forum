package com.suresoft.sw_test_forum.cwe_java.cwe_java.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJava;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJava cweJava) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJava.getCreatedDate())
                .lastModifiedDate(cweJava.getLastModifiedDate())
                .createdByIdx(cweJava.getCreatedByIdx())
                .lastModifiedByIdx(cweJava.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJava.getIdx())
                .auditBoard("CWE Java 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cweJava.getTitle()))
                .content(cweJava.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJava cweJava) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJava.getCreatedDate())
                .lastModifiedDate(cweJava.getLastModifiedDate())
                .createdByIdx(cweJava.getCreatedByIdx())
                .lastModifiedByIdx(cweJava.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJava.getIdx())
                .auditBoard("CWE Java 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cweJava.getTitle()))
                .content(cweJava.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJava cweJava) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJava.getCreatedDate())
                .lastModifiedDate(cweJava.getLastModifiedDate())
                .createdByIdx(cweJava.getCreatedByIdx())
                .lastModifiedByIdx(cweJava.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJava.getIdx())
                .auditBoard("CWE Java 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cweJava.getTitle()))
                .content(cweJava.getContent())
                .build());
    }
}