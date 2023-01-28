package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCpp;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCpp misraCpp) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCpp.getCreatedDate())
                .lastModifiedDate(misraCpp.getLastModifiedDate())
                .createdByIdx(misraCpp.getCreatedByIdx())
                .lastModifiedByIdx(misraCpp.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCpp.getIdx())
                .auditBoard("MISRA C++ 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraCpp.getTitle()))
                .content(misraCpp.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCpp misraCpp) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCpp.getCreatedDate())
                .lastModifiedDate(misraCpp.getLastModifiedDate())
                .createdByIdx(misraCpp.getCreatedByIdx())
                .lastModifiedByIdx(misraCpp.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCpp.getIdx())
                .auditBoard("MISRA C++ 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraCpp.getTitle()))
                .content(misraCpp.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCpp misraCpp) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCpp.getCreatedDate())
                .lastModifiedDate(misraCpp.getLastModifiedDate())
                .createdByIdx(misraCpp.getCreatedByIdx())
                .lastModifiedByIdx(misraCpp.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCpp.getIdx())
                .auditBoard("MISRA C++ 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraCpp.getTitle()))
                .content(misraCpp.getContent())
                .build());
    }
}