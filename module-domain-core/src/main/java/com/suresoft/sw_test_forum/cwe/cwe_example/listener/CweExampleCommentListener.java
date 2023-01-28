package com.suresoft.sw_test_forum.cwe.cwe_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweExampleComment cweExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExampleComment.getCreatedDate())
                .lastModifiedDate(cweExampleComment.getLastModifiedDate())
                .createdByIdx(cweExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExampleComment.getIdx())
                .auditBoard("CWE C/C++ 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweExampleComment.getCweExampleIdx()))
                .content(cweExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweExampleComment cweExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExampleComment.getCreatedDate())
                .lastModifiedDate(cweExampleComment.getLastModifiedDate())
                .createdByIdx(cweExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExampleComment.getIdx())
                .auditBoard("CWE C/C++ 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweExampleComment.getCweExampleIdx()))
                .content(cweExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweExampleComment cweExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweExampleComment.getCreatedDate())
                .lastModifiedDate(cweExampleComment.getLastModifiedDate())
                .createdByIdx(cweExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(cweExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweExampleComment.getIdx())
                .auditBoard("CWE C/C++ 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweExampleComment.getCweExampleIdx()))
                .content(cweExampleComment.getContent())
                .build());
    }
}