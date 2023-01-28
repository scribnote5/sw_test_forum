package com.suresoft.sw_test_forum.style_cop.style_cop.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopComment styleCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopComment.getCreatedDate())
                .lastModifiedDate(styleCopComment.getLastModifiedDate())
                .createdByIdx(styleCopComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopComment.getIdx())
                .auditBoard("C# Coding Convention 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(styleCopComment.getStyleCopIdx()))
                .content(styleCopComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCopComment styleCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopComment.getCreatedDate())
                .lastModifiedDate(styleCopComment.getLastModifiedDate())
                .createdByIdx(styleCopComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopComment.getIdx())
                .auditBoard("C# Coding Convention 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(styleCopComment.getStyleCopIdx()))
                .content(styleCopComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopComment styleCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopComment.getCreatedDate())
                .lastModifiedDate(styleCopComment.getLastModifiedDate())
                .createdByIdx(styleCopComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopComment.getIdx())
                .auditBoard("C# Coding Convention 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(styleCopComment.getStyleCopIdx()))
                .content(styleCopComment.getContent())
                .build());
    }
}