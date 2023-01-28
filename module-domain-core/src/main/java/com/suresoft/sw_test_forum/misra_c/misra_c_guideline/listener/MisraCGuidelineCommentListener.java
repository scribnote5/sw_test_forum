package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCGuidelineComment misraCGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuidelineComment.getIdx())
                .auditBoard("MISRA C 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCGuidelineComment.getMisraCGuidelineIdx()))
                .content(misraCGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCGuidelineComment misraCGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuidelineComment.getIdx())
                .auditBoard("MISRA C 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCGuidelineComment.getMisraCGuidelineIdx()))
                .content(misraCGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCGuidelineComment misraCGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuidelineComment.getIdx())
                .auditBoard("MISRA C 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCGuidelineComment.getMisraCGuidelineIdx()))
                .content(misraCGuidelineComment.getContent())
                .build());
    }
}