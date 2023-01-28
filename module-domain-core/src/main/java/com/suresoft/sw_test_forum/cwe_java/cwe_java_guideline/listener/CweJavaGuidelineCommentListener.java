package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweJavaGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaGuidelineComment cweJavaGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweJavaGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweJavaGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuidelineComment.getIdx())
                .auditBoard("CWE Java 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweJavaGuidelineComment.getCweJavaGuidelineIdx()))
                .content(cweJavaGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweJavaGuidelineComment cweJavaGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweJavaGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweJavaGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuidelineComment.getIdx())
                .auditBoard("CWE Java 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweJavaGuidelineComment.getCweJavaGuidelineIdx()))
                .content(cweJavaGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaGuidelineComment cweJavaGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweJavaGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweJavaGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuidelineComment.getIdx())
                .auditBoard("CWE Java 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweJavaGuidelineComment.getCweJavaGuidelineIdx()))
                .content(cweJavaGuidelineComment.getContent())
                .build());
    }
}