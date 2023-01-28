package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaExampleComment cweJavaExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExampleComment.getCreatedDate())
                .lastModifiedDate(cweJavaExampleComment.getLastModifiedDate())
                .createdByIdx(cweJavaExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExampleComment.getIdx())
                .auditBoard("CWE 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweJavaExampleComment.getCweJavaExampleIdx()))
                .content(cweJavaExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJavaExampleComment cweJavaExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExampleComment.getCreatedDate())
                .lastModifiedDate(cweJavaExampleComment.getLastModifiedDate())
                .createdByIdx(cweJavaExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExampleComment.getIdx())
                .auditBoard("CWE 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweJavaExampleComment.getCweJavaExampleIdx()))
                .content(cweJavaExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaExampleComment cweJavaExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaExampleComment.getCreatedDate())
                .lastModifiedDate(cweJavaExampleComment.getLastModifiedDate())
                .createdByIdx(cweJavaExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaExampleComment.getIdx())
                .auditBoard("CWE Java 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweJavaExampleComment.getCweJavaExampleIdx()))
                .content(cweJavaExampleComment.getContent())
                .build());
    }
}