package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolTroubleShootingCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolTroubleShootingCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolTroubleShootingComment toolTroubleShootingComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShootingComment.getCreatedDate())
                .lastModifiedDate(toolTroubleShootingComment.getLastModifiedDate())
                .createdByIdx(toolTroubleShootingComment.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShootingComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShootingComment.getIdx())
                .auditBoard("도구 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(toolTroubleShootingComment.getToolTroubleShootingIdx()))
                .content(toolTroubleShootingComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolTroubleShootingComment toolTroubleShootingComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShootingComment.getCreatedDate())
                .lastModifiedDate(toolTroubleShootingComment.getLastModifiedDate())
                .createdByIdx(toolTroubleShootingComment.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShootingComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShootingComment.getIdx())
                .auditBoard("도구 트러블슈팅 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(toolTroubleShootingComment.getToolTroubleShootingIdx()))
                .content(toolTroubleShootingComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolTroubleShootingComment toolTroubleShootingComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShootingComment.getCreatedDate())
                .lastModifiedDate(toolTroubleShootingComment.getLastModifiedDate())
                .createdByIdx(toolTroubleShootingComment.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShootingComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShootingComment.getIdx())
                .auditBoard("도구 트러블슈팅 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(toolTroubleShootingComment.getToolTroubleShootingIdx()))
                .content(toolTroubleShootingComment.getContent())
                .build());
    }
}