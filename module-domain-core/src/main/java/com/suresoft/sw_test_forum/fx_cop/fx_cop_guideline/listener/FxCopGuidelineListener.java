package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopGuideline fxCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuideline.getCreatedDate())
                .lastModifiedDate(fxCopGuideline.getLastModifiedDate())
                .createdByIdx(fxCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuideline.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(fxCopGuideline.getTitle()))
                .content(fxCopGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCopGuideline fxCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuideline.getCreatedDate())
                .lastModifiedDate(fxCopGuideline.getLastModifiedDate())
                .createdByIdx(fxCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuideline.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(fxCopGuideline.getTitle()))
                .content(fxCopGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCopGuideline fxCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuideline.getCreatedDate())
                .lastModifiedDate(fxCopGuideline.getLastModifiedDate())
                .createdByIdx(fxCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuideline.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(fxCopGuideline.getTitle()))
                .content(fxCopGuideline.getContent())
                .build());
    }
}