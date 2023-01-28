package com.suresoft.sw_test_forum.cwe.cwe.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweComment cweComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweComment.getCreatedDate())
                .lastModifiedDate(cweComment.getLastModifiedDate())
                .createdByIdx(cweComment.getCreatedByIdx())
                .lastModifiedByIdx(cweComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweComment.getIdx())
                .auditBoard("CWE C/C++ 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweComment.getCweIdx()))
                .content(cweComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweComment cweComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweComment.getCreatedDate())
                .lastModifiedDate(cweComment.getLastModifiedDate())
                .createdByIdx(cweComment.getCreatedByIdx())
                .lastModifiedByIdx(cweComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweComment.getIdx())
                .auditBoard("CWE C/C++ 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweComment.getCweIdx()))
                .content(cweComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweComment cweComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweComment.getCreatedDate())
                .lastModifiedDate(cweComment.getLastModifiedDate())
                .createdByIdx(cweComment.getCreatedByIdx())
                .lastModifiedByIdx(cweComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweComment.getIdx())
                .auditBoard("CWE C/C++ 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweComment.getCweIdx()))
                .content(cweComment.getContent())
                .build());
    }
}