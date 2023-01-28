package com.suresoft.sw_test_forum.admin_page.notice.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.notice.domain.Notice;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class NoticeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public NoticeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Notice notice) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(notice.getCreatedDate())
                .lastModifiedDate(notice.getLastModifiedDate())
                .createdByIdx(notice.getCreatedByIdx())
                .lastModifiedByIdx(notice.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(notice.getIdx())
                .auditBoard("공지사항")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(notice.getTitle()))
                .content(notice.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Notice notice) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(notice.getCreatedDate())
                .lastModifiedDate(notice.getLastModifiedDate())
                .createdByIdx(notice.getCreatedByIdx())
                .lastModifiedByIdx(notice.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(notice.getIdx())
                .auditBoard("공지사항")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(notice.getTitle()))
                .content(notice.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Notice notice) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(notice.getCreatedDate())
                .lastModifiedDate(notice.getLastModifiedDate())
                .createdByIdx(notice.getCreatedByIdx())
                .lastModifiedByIdx(notice.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(notice.getIdx())
                .auditBoard("공지사항")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(notice.getTitle()))
                .content(notice.getContent())
                .build());
    }
    
}
