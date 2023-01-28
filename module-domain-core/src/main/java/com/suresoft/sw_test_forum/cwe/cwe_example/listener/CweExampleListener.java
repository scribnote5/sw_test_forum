package com.suresoft.sw_test_forum.cwe.cwe_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweExample cweExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExample.getCreatedDate())
                .lastModifiedDate(cweExample.getLastModifiedDate())
                .createdByIdx(cweExample.getCreatedByIdx())
                .lastModifiedByIdx(cweExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExample.getIdx())
                .auditBoard("CWE C/C++ 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(cweExample.getTitle()))
                .content(cweExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweExample cweExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExample.getCreatedDate())
                .lastModifiedDate(cweExample.getLastModifiedDate())
                .createdByIdx(cweExample.getCreatedByIdx())
                .lastModifiedByIdx(cweExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExample.getIdx())
                .auditBoard("CWE C/C++ 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(cweExample.getTitle()))
                .content(cweExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweExample cweExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExample.getCreatedDate())
                .lastModifiedDate(cweExample.getLastModifiedDate())
                .createdByIdx(cweExample.getCreatedByIdx())
                .lastModifiedByIdx(cweExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExample.getIdx())
                .auditBoard("CWE C/C++ 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(cweExample.getTitle()))
                .content(cweExample.getContent())
                .build());
    }

}
