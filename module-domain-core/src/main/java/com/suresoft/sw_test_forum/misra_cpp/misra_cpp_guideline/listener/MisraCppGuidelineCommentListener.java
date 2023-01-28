package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppGuidelineCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppGuidelineCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppGuidelineComment misraCppGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCppGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCppGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuidelineComment.getIdx())
                .auditBoard("MISRA C++ 가이드라인 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCppGuidelineComment.getMisraCppGuidelineIdx()))
                .content(misraCppGuidelineComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCppGuidelineComment misraCppGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCppGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCppGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuidelineComment.getIdx())
                .auditBoard("MISRA C++ 가이드라인 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCppGuidelineComment.getMisraCppGuidelineIdx()))
                .content(misraCppGuidelineComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppGuidelineComment misraCppGuidelineComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuidelineComment.getCreatedDate())
                .lastModifiedDate(misraCppGuidelineComment.getLastModifiedDate())
                .createdByIdx(misraCppGuidelineComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuidelineComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuidelineComment.getIdx())
                .auditBoard("MISRA C++ 가이드라인 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCppGuidelineComment.getMisraCppGuidelineIdx()))
                .content(misraCppGuidelineComment.getContent())
                .build());
    }
}