package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsExampleComment javaCodeConventionsExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExampleComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExampleComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExampleComment.getIdx())
                .auditBoard("Java Code Conventions 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(javaCodeConventionsExampleComment.getJavaCodeConventionsExampleIdx()))
                .content(javaCodeConventionsExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventionsExampleComment javaCodeConventionsExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExampleComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExampleComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExampleComment.getIdx())
                .auditBoard("Java Code Conventions 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(javaCodeConventionsExampleComment.getJavaCodeConventionsExampleIdx()))
                .content(javaCodeConventionsExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsExampleComment javaCodeConventionsExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsExampleComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsExampleComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsExampleComment.getIdx())
                .auditBoard("Java Code Conventions 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(javaCodeConventionsExampleComment.getJavaCodeConventionsExampleIdx()))
                .content(javaCodeConventionsExampleComment.getContent())
                .build());
    }
}