package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppComment misraCppComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppComment.getCreatedDate())
                .lastModifiedDate(misraCppComment.getLastModifiedDate())
                .createdByIdx(misraCppComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppComment.getIdx())
                .auditBoard("MISRA C++ 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCppComment.getMisraCppIdx()))
                .content(misraCppComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCppComment misraCppComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppComment.getCreatedDate())
                .lastModifiedDate(misraCppComment.getLastModifiedDate())
                .createdByIdx(misraCppComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppComment.getIdx())
                .auditBoard("MISRA C++ 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCppComment.getMisraCppIdx()))
                .content(misraCppComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppComment misraCppComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppComment.getCreatedDate())
                .lastModifiedDate(misraCppComment.getLastModifiedDate())
                .createdByIdx(misraCppComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppComment.getIdx())
                .auditBoard("MISRA C++ 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCppComment.getMisraCppIdx()))
                .content(misraCppComment.getContent())
                .build());
    }
}