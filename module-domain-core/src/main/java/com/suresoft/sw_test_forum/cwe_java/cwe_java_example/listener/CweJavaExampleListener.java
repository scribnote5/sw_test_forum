package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaExample cweJavaExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExample.getCreatedDate())
                .lastModifiedDate(cweJavaExample.getLastModifiedDate())
                .createdByIdx(cweJavaExample.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExample.getIdx())
                .auditBoard("CWE Java 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cweJavaExample.getTitle()))
                .content(cweJavaExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJavaExample cweJavaExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExample.getCreatedDate())
                .lastModifiedDate(cweJavaExample.getLastModifiedDate())
                .createdByIdx(cweJavaExample.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExample.getIdx())
                .auditBoard("CWE Java 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cweJavaExample.getTitle()))
                .content(cweJavaExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaExample cweJavaExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExample.getCreatedDate())
                .lastModifiedDate(cweJavaExample.getLastModifiedDate())
                .createdByIdx(cweJavaExample.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExample.getIdx())
                .auditBoard("CWE Java 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cweJavaExample.getTitle()))
                .content(cweJavaExample.getContent())
                .build());
    }

}
