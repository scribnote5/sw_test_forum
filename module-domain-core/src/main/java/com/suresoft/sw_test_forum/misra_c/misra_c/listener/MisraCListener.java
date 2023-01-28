package com.suresoft.sw_test_forum.misra_c.misra_c.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraC;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraC misraC) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraC.getCreatedDate())
                .lastModifiedDate(misraC.getLastModifiedDate())
                .createdByIdx(misraC.getCreatedByIdx())
                .lastModifiedByIdx(misraC.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraC.getIdx())
                .auditBoard("MISRA C 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraC.getTitle()))
                .content(misraC.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraC misraC) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraC.getCreatedDate())
                .lastModifiedDate(misraC.getLastModifiedDate())
                .createdByIdx(misraC.getCreatedByIdx())
                .lastModifiedByIdx(misraC.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraC.getIdx())
                .auditBoard("MISRA C 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraC.getTitle()))
                .content(misraC.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraC misraC) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraC.getCreatedDate())
                .lastModifiedDate(misraC.getLastModifiedDate())
                .createdByIdx(misraC.getCreatedByIdx())
                .lastModifiedByIdx(misraC.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraC.getIdx())
                .auditBoard("MISRA C 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraC.getTitle()))
                .content(misraC.getContent())
                .build());
    }
}