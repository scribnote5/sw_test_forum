package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsGuidelineComment javaCodeConventionsGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuidelineComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuidelineComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuidelineComment.getIdx())
                .auditBoard("Java Code Conventions 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(javaCodeConventionsGuidelineComment.getJavaCodeConventionsGuidelineIdx()))
                .content(javaCodeConventionsGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventionsGuidelineComment javaCodeConventionsGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuidelineComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuidelineComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuidelineComment.getIdx())
                .auditBoard("Java Code Conventions 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(javaCodeConventionsGuidelineComment.getJavaCodeConventionsGuidelineIdx()))
                .content(javaCodeConventionsGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsGuidelineComment javaCodeConventionsGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuidelineComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuidelineComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuidelineComment.getIdx())
                .auditBoard("Java Code Conventions 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(javaCodeConventionsGuidelineComment.getJavaCodeConventionsGuidelineIdx()))
                .content(javaCodeConventionsGuidelineComment.getContent())
                .build());
    }
}