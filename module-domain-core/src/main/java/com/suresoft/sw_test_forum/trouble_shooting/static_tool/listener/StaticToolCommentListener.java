package com.suresoft.sw_test_forum.trouble_shooting.static_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StaticToolCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StaticToolCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StaticToolComment staticToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticToolComment.getCreatedDate())
                .lastModifiedDate(staticToolComment.getLastModifiedDate())
                .createdByIdx(staticToolComment.getCreatedByIdx())
                .lastModifiedByIdx(staticToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticToolComment.getIdx())
                .auditBoard("STATIC 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(staticToolComment.getStaticToolIdx()))
                .content(staticToolComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StaticToolComment staticToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticToolComment.getCreatedDate())
                .lastModifiedDate(staticToolComment.getLastModifiedDate())
                .createdByIdx(staticToolComment.getCreatedByIdx())
                .lastModifiedByIdx(staticToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticToolComment.getIdx())
                .auditBoard("STATIC 트러블슈팅 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(staticToolComment.getStaticToolIdx()))
                .content(staticToolComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StaticToolComment staticToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticToolComment.getCreatedDate())
                .lastModifiedDate(staticToolComment.getLastModifiedDate())
                .createdByIdx(staticToolComment.getCreatedByIdx())
                .lastModifiedByIdx(staticToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticToolComment.getIdx())
                .auditBoard("STATIC 트러블슈팅 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(staticToolComment.getStaticToolIdx()))
                .content(staticToolComment.getContent())
                .build());
    }
}