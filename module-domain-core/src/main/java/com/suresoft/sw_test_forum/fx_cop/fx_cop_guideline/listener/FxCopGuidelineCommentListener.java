package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopGuidelineComment fxCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(fxCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(fxCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuidelineComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(fxCopGuidelineComment.getFxCopGuidelineIdx()))
                .content(fxCopGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCopGuidelineComment fxCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(fxCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(fxCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuidelineComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(fxCopGuidelineComment.getFxCopGuidelineIdx()))
                .content(fxCopGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCopGuidelineComment fxCopGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuidelineComment.getCreatedDate())
                .lastModifiedDate(fxCopGuidelineComment.getLastModifiedDate())
                .createdByIdx(fxCopGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuidelineComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(fxCopGuidelineComment.getFxCopGuidelineIdx()))
                .content(fxCopGuidelineComment.getContent())
                .build());
    }
}