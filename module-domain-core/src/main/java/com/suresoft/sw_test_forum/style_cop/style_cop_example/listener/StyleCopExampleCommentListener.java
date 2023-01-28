package com.suresoft.sw_test_forum.style_cop.style_cop_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopExampleComment styleCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExampleComment.getCreatedDate())
                .lastModifiedDate(styleCopExampleComment.getLastModifiedDate())
                .createdByIdx(styleCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExampleComment.getIdx())
                .auditBoard("StyleCop예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(styleCopExampleComment.getStyleCopExampleIdx()))
                .content(styleCopExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCopExampleComment styleCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExampleComment.getCreatedDate())
                .lastModifiedDate(styleCopExampleComment.getLastModifiedDate())
                .createdByIdx(styleCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExampleComment.getIdx())
                .auditBoard("StyleCop예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(styleCopExampleComment.getStyleCopExampleIdx()))
                .content(styleCopExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopExampleComment styleCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopExampleComment.getCreatedDate())
                .lastModifiedDate(styleCopExampleComment.getLastModifiedDate())
                .createdByIdx(styleCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopExampleComment.getIdx())
                .auditBoard("C# Coding Convention 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(styleCopExampleComment.getStyleCopExampleIdx()))
                .content(styleCopExampleComment.getContent())
                .build());
    }
}