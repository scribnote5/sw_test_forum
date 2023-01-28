package com.suresoft.sw_test_forum.fx_cop.fx_cop.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopComment fxCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopComment.getCreatedDate())
                .lastModifiedDate(fxCopComment.getLastModifiedDate())
                .createdByIdx(fxCopComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(fxCopComment.getFxCopIdx()))
                .content(fxCopComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCopComment fxCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopComment.getCreatedDate())
                .lastModifiedDate(fxCopComment.getLastModifiedDate())
                .createdByIdx(fxCopComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(fxCopComment.getFxCopIdx()))
                .content(fxCopComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCopComment fxCopComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopComment.getCreatedDate())
                .lastModifiedDate(fxCopComment.getLastModifiedDate())
                .createdByIdx(fxCopComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(fxCopComment.getFxCopIdx()))
                .content(fxCopComment.getContent())
                .build());
    }
}