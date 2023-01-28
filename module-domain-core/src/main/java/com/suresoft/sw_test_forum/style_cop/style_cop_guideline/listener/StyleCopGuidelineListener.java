package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopGuideline styleCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuideline.getCreatedDate())
                .lastModifiedDate(styleCopGuideline.getLastModifiedDate())
                .createdByIdx(styleCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuideline.getIdx())
                .auditBoard("C# Coding Convention 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(styleCopGuideline.getTitle()))
                .content(styleCopGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCopGuideline styleCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuideline.getCreatedDate())
                .lastModifiedDate(styleCopGuideline.getLastModifiedDate())
                .createdByIdx(styleCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuideline.getIdx())
                .auditBoard("C# Coding Convention 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(styleCopGuideline.getTitle()))
                .content(styleCopGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopGuideline styleCopGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuideline.getCreatedDate())
                .lastModifiedDate(styleCopGuideline.getLastModifiedDate())
                .createdByIdx(styleCopGuideline.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuideline.getIdx())
                .auditBoard("C# Coding Convention 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(styleCopGuideline.getTitle()))
                .content(styleCopGuideline.getContent())
                .build());
    }
}