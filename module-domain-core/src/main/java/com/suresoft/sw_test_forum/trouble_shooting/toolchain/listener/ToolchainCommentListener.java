package com.suresoft.sw_test_forum.trouble_shooting.toolchain.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolchainCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolchainCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolchainComment toolchainComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchainComment.getCreatedDate())
                .lastModifiedDate(toolchainComment.getLastModifiedDate())
                .createdByIdx(toolchainComment.getCreatedByIdx())
                .lastModifiedByIdx(toolchainComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchainComment.getIdx())
                .auditBoard("툴체인 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(toolchainComment.getToolchainIdx()))
                .content(toolchainComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolchainComment toolchainComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchainComment.getCreatedDate())
                .lastModifiedDate(toolchainComment.getLastModifiedDate())
                .createdByIdx(toolchainComment.getCreatedByIdx())
                .lastModifiedByIdx(toolchainComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchainComment.getIdx())
                .auditBoard("툴체인 트러블슈팅 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(toolchainComment.getToolchainIdx()))
                .content(toolchainComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolchainComment toolchainComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchainComment.getCreatedDate())
                .lastModifiedDate(toolchainComment.getLastModifiedDate())
                .createdByIdx(toolchainComment.getCreatedByIdx())
                .lastModifiedByIdx(toolchainComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchainComment.getIdx())
                .auditBoard("툴체인 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(toolchainComment.getToolchainIdx()))
                .content(toolchainComment.getContent())
                .build());
    }
}