package com.suresoft.sw_test_forum.tool.tool_usage_method.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolUsageMethodCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolUsageMethodCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolUsageMethodComment toolUsageMethodComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethodComment.getCreatedDate())
                .lastModifiedDate(toolUsageMethodComment.getLastModifiedDate())
                .createdByIdx(toolUsageMethodComment.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethodComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethodComment.getIdx())
                .auditBoard("도구 사용 방법 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(toolUsageMethodComment.getToolUsageMethodIdx()))
                .content(toolUsageMethodComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolUsageMethodComment toolUsageMethodComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethodComment.getCreatedDate())
                .lastModifiedDate(toolUsageMethodComment.getLastModifiedDate())
                .createdByIdx(toolUsageMethodComment.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethodComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethodComment.getIdx())
                .auditBoard("도구 사용 방법 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(toolUsageMethodComment.getToolUsageMethodIdx()))
                .content(toolUsageMethodComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolUsageMethodComment toolUsageMethodComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolUsageMethodComment.getCreatedDate())
                .lastModifiedDate(toolUsageMethodComment.getLastModifiedDate())
                .createdByIdx(toolUsageMethodComment.getCreatedByIdx())
                .lastModifiedByIdx(toolUsageMethodComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolUsageMethodComment.getIdx())
                .auditBoard("도구 사용 방법 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(toolUsageMethodComment.getToolUsageMethodIdx()))
                .content(toolUsageMethodComment.getContent())
                .build());
    }
}