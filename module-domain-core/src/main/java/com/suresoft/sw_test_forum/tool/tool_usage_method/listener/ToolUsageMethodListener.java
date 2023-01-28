package com.suresoft.sw_test_forum.tool.tool_usage_method.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethod;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolUsageMethodListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolUsageMethodListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolUsageMethod toolUsageMethod) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethod.getCreatedDate())
                .lastModifiedDate(toolUsageMethod.getLastModifiedDate())
                .createdByIdx(toolUsageMethod.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethod.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethod.getIdx())
                .auditBoard("도구 사용 방법")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(toolUsageMethod.getTitle()))
                .content(toolUsageMethod.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolUsageMethod toolUsageMethod) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethod.getCreatedDate())
                .lastModifiedDate(toolUsageMethod.getLastModifiedDate())
                .createdByIdx(toolUsageMethod.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethod.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethod.getIdx())
                .auditBoard("도구 사용 방법")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(toolUsageMethod.getTitle()))
                .content(toolUsageMethod.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolUsageMethod toolUsageMethod) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethod.getCreatedDate())
                .lastModifiedDate(toolUsageMethod.getLastModifiedDate())
                .createdByIdx(toolUsageMethod.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethod.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethod.getIdx())
                .auditBoard("도구 사용 방법")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(toolUsageMethod.getTitle()))
                .content(toolUsageMethod.getContent())
                .build());
    }
}