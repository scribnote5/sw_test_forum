package com.suresoft.sw_test_forum.fx_cop.fx_cop.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCop;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCop fxCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCop.getCreatedDate())
                .lastModifiedDate(fxCop.getLastModifiedDate())
                .createdByIdx(fxCop.getCreatedByIdx())
                .lastModifiedByIdx(fxCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCop.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(fxCop.getTitle()))
                .content(fxCop.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCop fxCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCop.getCreatedDate())
                .lastModifiedDate(fxCop.getLastModifiedDate())
                .createdByIdx(fxCop.getCreatedByIdx())
                .lastModifiedByIdx(fxCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCop.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(fxCop.getTitle()))
                .content(fxCop.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCop fxCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCop.getCreatedDate())
                .lastModifiedDate(fxCop.getLastModifiedDate())
                .createdByIdx(fxCop.getCreatedByIdx())
                .lastModifiedByIdx(fxCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCop.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(fxCop.getTitle()))
                .content(fxCop.getContent())
                .build());
    }
}