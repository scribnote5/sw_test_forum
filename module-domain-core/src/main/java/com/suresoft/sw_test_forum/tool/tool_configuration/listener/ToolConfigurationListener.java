package com.suresoft.sw_test_forum.tool.tool_configuration.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfiguration;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolConfigurationListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolConfigurationListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolConfiguration toolConfiguration) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfiguration.getCreatedDate())
                .lastModifiedDate(toolConfiguration.getLastModifiedDate())
                .createdByIdx(toolConfiguration.getCreatedByIdx())
                .lastModifiedByIdx(toolConfiguration.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfiguration.getIdx())
                .auditBoard("도구 환경 설정")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(toolConfiguration.getTitle()))
                .content(toolConfiguration.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolConfiguration toolConfiguration) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfiguration.getCreatedDate())
                .lastModifiedDate(toolConfiguration.getLastModifiedDate())
                .createdByIdx(toolConfiguration.getCreatedByIdx())
                .lastModifiedByIdx(toolConfiguration.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfiguration.getIdx())
                .auditBoard("도구 트러블슈팅")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(toolConfiguration.getTitle()))
                .content(toolConfiguration.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolConfiguration toolConfiguration) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfiguration.getCreatedDate())
                .lastModifiedDate(toolConfiguration.getLastModifiedDate())
                .createdByIdx(toolConfiguration.getCreatedByIdx())
                .lastModifiedByIdx(toolConfiguration.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfiguration.getIdx())
                .auditBoard("도구 환경 설정")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(toolConfiguration.getTitle()))
                .content(toolConfiguration.getContent())
                .build());
    }
}