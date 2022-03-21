package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CoverToolCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CoverToolCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CoverToolComment coverToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverToolComment.getCreatedDate())
                .lastModifiedDate(coverToolComment.getLastModifiedDate())
                .createdByIdx(coverToolComment.getCreatedByIdx())
                .lastModifiedByIdx(coverToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverToolComment.getIdx())
                .auditBoard("COVER 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(coverToolComment.getCoverToolIdx()))
                .content(coverToolComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CoverToolComment coverToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverToolComment.getCreatedDate())
                .lastModifiedDate(coverToolComment.getLastModifiedDate())
                .createdByIdx(coverToolComment.getCreatedByIdx())
                .lastModifiedByIdx(coverToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverToolComment.getIdx())
                .auditBoard("COVER 트러블슈팅 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(coverToolComment.getCoverToolIdx()))
                .content(coverToolComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CoverToolComment coverToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverToolComment.getCreatedDate())
                .lastModifiedDate(coverToolComment.getLastModifiedDate())
                .createdByIdx(coverToolComment.getCreatedByIdx())
                .lastModifiedByIdx(coverToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverToolComment.getIdx())
                .auditBoard("COVER 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(coverToolComment.getCoverToolIdx()))
                .content(coverToolComment.getContent())
                .build());
    }
}