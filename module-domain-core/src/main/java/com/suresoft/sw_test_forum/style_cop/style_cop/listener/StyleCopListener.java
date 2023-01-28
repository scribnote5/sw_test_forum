package com.suresoft.sw_test_forum.style_cop.style_cop.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCop;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCop styleCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCop.getCreatedDate())
                .lastModifiedDate(styleCop.getLastModifiedDate())
                .createdByIdx(styleCop.getCreatedByIdx())
                .lastModifiedByIdx(styleCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCop.getIdx())
                .auditBoard("C# Coding Convention 규칙")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(styleCop.getTitle()))
                .content(styleCop.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCop styleCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCop.getCreatedDate())
                .lastModifiedDate(styleCop.getLastModifiedDate())
                .createdByIdx(styleCop.getCreatedByIdx())
                .lastModifiedByIdx(styleCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCop.getIdx())
                .auditBoard("C# Coding Convention 규칙")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(styleCop.getTitle()))
                .content(styleCop.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCop styleCop) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCop.getCreatedDate())
                .lastModifiedDate(styleCop.getLastModifiedDate())
                .createdByIdx(styleCop.getCreatedByIdx())
                .lastModifiedByIdx(styleCop.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCop.getIdx())
                .auditBoard("C# Coding Convention 규칙")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(styleCop.getTitle()))
                .content(styleCop.getContent())
                .build());
    }
}