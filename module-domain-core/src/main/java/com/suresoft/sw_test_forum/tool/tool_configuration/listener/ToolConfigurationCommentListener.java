package com.suresoft.sw_test_forum.tool.tool_configuration.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolConfigurationCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolConfigurationCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolConfigurationComment toolConfigurationComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfigurationComment.getCreatedDate())
                .lastModifiedDate(toolConfigurationComment.getLastModifiedDate())
                .createdByIdx(toolConfigurationComment.getCreatedByIdx())
                .lastModifiedByIdx(toolConfigurationComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfigurationComment.getIdx())
                .auditBoard("도구 환경 설정 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(toolConfigurationComment.getToolConfigurationIdx()))
                .content(toolConfigurationComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolConfigurationComment toolConfigurationComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfigurationComment.getCreatedDate())
                .lastModifiedDate(toolConfigurationComment.getLastModifiedDate())
                .createdByIdx(toolConfigurationComment.getCreatedByIdx())
                .lastModifiedByIdx(toolConfigurationComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfigurationComment.getIdx())
                .auditBoard("도구 환경 설정 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(toolConfigurationComment.getToolConfigurationIdx()))
                .content(toolConfigurationComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolConfigurationComment toolConfigurationComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolConfigurationComment.getCreatedDate())
                .lastModifiedDate(toolConfigurationComment.getLastModifiedDate())
                .createdByIdx(toolConfigurationComment.getCreatedByIdx())
                .lastModifiedByIdx(toolConfigurationComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolConfigurationComment.getIdx())
                .auditBoard("도구 환경 설정 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(toolConfigurationComment.getToolConfigurationIdx()))
                .content(toolConfigurationComment.getContent())
                .build());
    }
}