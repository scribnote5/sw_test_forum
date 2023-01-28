package com.suresoft.sw_test_forum.cwe.cwe_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CweGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweGuidelineComment cweGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuidelineComment.getIdx())
                .auditBoard("CWE C/C++ 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(cweGuidelineComment.getCweGuidelineIdx()))
                .content(cweGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CweGuidelineComment cweGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuidelineComment.getIdx())
                .auditBoard("CWE C/C++ 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(cweGuidelineComment.getCweGuidelineIdx()))
                .content(cweGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CweGuidelineComment cweGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuidelineComment.getCreatedDate())
                .lastModifiedDate(cweGuidelineComment.getLastModifiedDate())
                .createdByIdx(cweGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(cweGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuidelineComment.getIdx())
                .auditBoard("CWE C/C++ 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(cweGuidelineComment.getCweGuidelineIdx()))
                .content(cweGuidelineComment.getContent())
                .build());
    }
}