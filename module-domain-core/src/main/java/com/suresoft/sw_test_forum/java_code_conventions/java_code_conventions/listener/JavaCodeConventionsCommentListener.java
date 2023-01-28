package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class JavaCodeConventionsCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsComment javaCodeConventionsComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsComment.getIdx())
                .auditBoard("Java Code Conventions 규칙 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(javaCodeConventionsComment.getJavaCodeConventionsIdx()))
                .content(javaCodeConventionsComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(JavaCodeConventionsComment javaCodeConventionsComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsComment.getIdx())
                .auditBoard("Java Code Conventions 규칙 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(javaCodeConventionsComment.getJavaCodeConventionsIdx()))
                .content(javaCodeConventionsComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsComment javaCodeConventionsComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsComment.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsComment.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsComment.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsComment.getIdx())
                .auditBoard("Java Code Conventions 규칙 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(javaCodeConventionsComment.getJavaCodeConventionsIdx()))
                .content(javaCodeConventionsComment.getContent())
                .build());
    }
}