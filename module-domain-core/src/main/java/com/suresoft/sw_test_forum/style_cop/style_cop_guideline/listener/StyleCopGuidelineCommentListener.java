package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StyleCopGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopGuidelineComment styleCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(styleCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(styleCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuidelineComment.getIdx())
                .auditBoard("C# Coding Convention 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(styleCopGuidelineComment.getStyleCopGuidelineIdx()))
                .content(styleCopGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StyleCopGuidelineComment styleCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(styleCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(styleCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuidelineComment.getIdx())
                .auditBoard("C# Coding Convention 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(styleCopGuidelineComment.getStyleCopGuidelineIdx()))
                .content(styleCopGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopGuidelineComment styleCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(styleCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(styleCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuidelineComment.getIdx())
                .auditBoard("C# Coding Convention 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(styleCopGuidelineComment.getStyleCopGuidelineIdx()))
                .content(styleCopGuidelineComment.getContent())
                .build());
    }
}