package com.suresoft.sw_test_forum.left_reference.dynamic_test.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DynamicTestCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public DynamicTestCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(DynamicTestComment dynamicTestComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTestComment.getCreatedDate())
                .lastModifiedDate(dynamicTestComment.getLastModifiedDate())
                .createdByIdx(dynamicTestComment.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTestComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTestComment.getIdx())
                .auditBoard("동적시험 미달성 코드 분석 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(dynamicTestComment.getDynamicTestIdx()))
                .content(dynamicTestComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(DynamicTestComment dynamicTestComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTestComment.getCreatedDate())
                .lastModifiedDate(dynamicTestComment.getLastModifiedDate())
                .createdByIdx(dynamicTestComment.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTestComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTestComment.getIdx())
                .auditBoard("동적시험 미달성 코드 분석 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(dynamicTestComment.getDynamicTestIdx()))
                .content(dynamicTestComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(DynamicTestComment dynamicTestComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTestComment.getCreatedDate())
                .lastModifiedDate(dynamicTestComment.getLastModifiedDate())
                .createdByIdx(dynamicTestComment.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTestComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTestComment.getIdx())
                .auditBoard("동적시험 미달성 코드 분석 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(dynamicTestComment.getDynamicTestIdx()))
                .content(dynamicTestComment.getContent())
                .build());
    }
}