package com.suresoft.sw_test_forum.misra_c.misra_c_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExampleComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCExampleCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCExampleCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCExampleComment misraCExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExampleComment.getCreatedDate())
                .lastModifiedDate(misraCExampleComment.getLastModifiedDate())
                .createdByIdx(misraCExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExampleComment.getIdx())
                .auditBoard("MISRA C 예제 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(misraCExampleComment.getMisraCExampleIdx()))
                .content(misraCExampleComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCExampleComment misraCExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExampleComment.getCreatedDate())
                .lastModifiedDate(misraCExampleComment.getLastModifiedDate())
                .createdByIdx(misraCExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExampleComment.getIdx())
                .auditBoard("MISRA C 예제 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(misraCExampleComment.getMisraCExampleIdx()))
                .content(misraCExampleComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCExampleComment misraCExampleComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExampleComment.getCreatedDate())
                .lastModifiedDate(misraCExampleComment.getLastModifiedDate())
                .createdByIdx(misraCExampleComment.getCreatedByIdx())
                .lastModifiedByIdx(misraCExampleComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExampleComment.getIdx())
                .auditBoard("MISRA C 예제 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(misraCExampleComment.getMisraCExampleIdx()))
                .content(misraCExampleComment.getContent())
                .build());
    }
}