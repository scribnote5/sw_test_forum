package com.suresoft.sw_test_forum.cwe.cwe.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe.domain.Cwe;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Cwe cwe) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cwe.getCreatedDate())
                .lastModifiedDate(cwe.getLastModifiedDate())
                .createdByIdx(cwe.getCreatedByIdx())
                .lastModifiedByIdx(cwe.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cwe.getIdx())
                .auditBoard("CWE C/C++ 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cwe.getTitle()))
                .content(cwe.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Cwe cwe) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cwe.getCreatedDate())
                .lastModifiedDate(cwe.getLastModifiedDate())
                .createdByIdx(cwe.getCreatedByIdx())
                .lastModifiedByIdx(cwe.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cwe.getIdx())
                .auditBoard("CWE C/C++ 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cwe.getTitle()))
                .content(cwe.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Cwe cwe) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cwe.getCreatedDate())
                .lastModifiedDate(cwe.getLastModifiedDate())
                .createdByIdx(cwe.getCreatedByIdx())
                .lastModifiedByIdx(cwe.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cwe.getIdx())
                .auditBoard("CWE C/C++ 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cwe.getTitle()))
                .content(cwe.getContent())
                .build());
    }
}