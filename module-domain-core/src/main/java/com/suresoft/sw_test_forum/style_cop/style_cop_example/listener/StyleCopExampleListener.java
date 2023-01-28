package com.suresoft.sw_test_forum.style_cop.style_cop_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopExample styleCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExample.getCreatedDate())
                .lastModifiedDate(styleCopExample.getLastModifiedDate())
                .createdByIdx(styleCopExample.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExample.getIdx())
                .auditBoard("C# Coding Convention 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(styleCopExample.getTitle()))
                .content(styleCopExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCopExample styleCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExample.getCreatedDate())
                .lastModifiedDate(styleCopExample.getLastModifiedDate())
                .createdByIdx(styleCopExample.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExample.getIdx())
                .auditBoard("C# Coding Convention 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(styleCopExample.getTitle()))
                .content(styleCopExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopExample styleCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExample.getCreatedDate())
                .lastModifiedDate(styleCopExample.getLastModifiedDate())
                .createdByIdx(styleCopExample.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExample.getIdx())
                .auditBoard("C# Coding Convention 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(styleCopExample.getTitle()))
                .content(styleCopExample.getContent())
                .build());
    }
}
