package com.suresoft.sw_test_forum.cwe_java.cwe_java.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaComment cweJavaComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaComment.getCreatedDate())
                .lastModifiedDate(cweJavaComment.getLastModifiedDate())
                .createdByIdx(cweJavaComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaComment.getIdx())
                .auditBoard("CWE Java 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweJavaComment.getCweJavaIdx()))
                .content(cweJavaComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJavaComment cweJavaComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaComment.getCreatedDate())
                .lastModifiedDate(cweJavaComment.getLastModifiedDate())
                .createdByIdx(cweJavaComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaComment.getIdx())
                .auditBoard("CWE Java 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweJavaComment.getCweJavaIdx()))
                .content(cweJavaComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaComment cweJavaComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaComment.getCreatedDate())
                .lastModifiedDate(cweJavaComment.getLastModifiedDate())
                .createdByIdx(cweJavaComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaComment.getIdx())
                .auditBoard("CWE Java 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweJavaComment.getCweJavaIdx()))
                .content(cweJavaComment.getContent())
                .build());
    }
}