package com.suresoft.sw_test_forum.admin_page.notice.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeComment;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class NoticeCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public NoticeCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(NoticeComment noticeComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(noticeComment.getCreatedDate())
                .lastModifiedDate(noticeComment.getLastModifiedDate())
                .createdByIdx(noticeComment.getCreatedByIdx())
                .lastModifiedByIdx(noticeComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(noticeComment.getIdx())
                .auditBoard("공지사항 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(noticeComment.getNoticeIdx()))
                .content(noticeComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(NoticeComment noticeComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(noticeComment.getCreatedDate())
                .lastModifiedDate(noticeComment.getLastModifiedDate())
                .createdByIdx(noticeComment.getCreatedByIdx())
                .lastModifiedByIdx(noticeComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(noticeComment.getIdx())
                .auditBoard("공지사항 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(noticeComment.getNoticeIdx()))
                .content(noticeComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(NoticeComment noticeComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(noticeComment.getCreatedDate())
                .lastModifiedDate(noticeComment.getLastModifiedDate())
                .createdByIdx(noticeComment.getCreatedByIdx())
                .lastModifiedByIdx(noticeComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(noticeComment.getIdx())
                .auditBoard("공지사항 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(noticeComment.getNoticeIdx()))
                .content(noticeComment.getContent())
                .build());
    }
}