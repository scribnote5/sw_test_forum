package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppExampleComment misraCppExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExampleComment.getCreatedDate())
                .lastModifiedDate(misraCppExampleComment.getLastModifiedDate())
                .createdByIdx(misraCppExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExampleComment.getIdx())
                .auditBoard("MISRA C++ 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCppExampleComment.getMisraCppExampleIdx()))
                .content(misraCppExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCppExampleComment misraCppExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExampleComment.getCreatedDate())
                .lastModifiedDate(misraCppExampleComment.getLastModifiedDate())
                .createdByIdx(misraCppExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExampleComment.getIdx())
                .auditBoard("MISRA C++ 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCppExampleComment.getMisraCppExampleIdx()))
                .content(misraCppExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppExampleComment misraCppExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExampleComment.getCreatedDate())
                .lastModifiedDate(misraCppExampleComment.getLastModifiedDate())
                .createdByIdx(misraCppExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExampleComment.getIdx())
                .auditBoard("MISRA C++ 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCppExampleComment.getMisraCppExampleIdx()))
                .content(misraCppExampleComment.getContent())
                .build());
    }
}