package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopExampleComment fxCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExampleComment.getCreatedDate())
                .lastModifiedDate(fxCopExampleComment.getLastModifiedDate())
                .createdByIdx(fxCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExampleComment.getIdx())
                .auditBoard("FxCop예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(fxCopExampleComment.getFxCopExampleIdx()))
                .content(fxCopExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCopExampleComment fxCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExampleComment.getCreatedDate())
                .lastModifiedDate(fxCopExampleComment.getLastModifiedDate())
                .createdByIdx(fxCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExampleComment.getIdx())
                .auditBoard("FxCop예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(fxCopExampleComment.getFxCopExampleIdx()))
                .content(fxCopExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCopExampleComment fxCopExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExampleComment.getCreatedDate())
                .lastModifiedDate(fxCopExampleComment.getLastModifiedDate())
                .createdByIdx(fxCopExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExampleComment.getIdx())
                .auditBoard(".NET Framework Design Guideline 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(fxCopExampleComment.getFxCopExampleIdx()))
                .content(fxCopExampleComment.getContent())
                .build());
    }
}