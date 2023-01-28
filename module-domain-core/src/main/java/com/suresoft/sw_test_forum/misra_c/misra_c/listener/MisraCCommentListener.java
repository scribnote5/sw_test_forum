package com.suresoft.sw_test_forum.misra_c.misra_c.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCComment misraCComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCComment.getCreatedDate())
                .lastModifiedDate(misraCComment.getLastModifiedDate())
                .createdByIdx(misraCComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCComment.getIdx())
                .auditBoard("MISRA C 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCComment.getMisraCIdx()))
                .content(misraCComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCComment misraCComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCComment.getCreatedDate())
                .lastModifiedDate(misraCComment.getLastModifiedDate())
                .createdByIdx(misraCComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCComment.getIdx())
                .auditBoard("MISRA C 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCComment.getMisraCIdx()))
                .content(misraCComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCComment misraCComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCComment.getCreatedDate())
                .lastModifiedDate(misraCComment.getLastModifiedDate())
                .createdByIdx(misraCComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCComment.getIdx())
                .auditBoard("MISRA C 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCComment.getMisraCIdx()))
                .content(misraCComment.getContent())
                .build());
    }
}