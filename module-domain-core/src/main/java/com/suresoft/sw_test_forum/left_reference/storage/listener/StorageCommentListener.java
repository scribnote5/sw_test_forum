package com.suresoft.sw_test_forum.left_reference.storage.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StorageCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StorageCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StorageComment storageComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storageComment.getCreatedDate())
                .lastModifiedDate(storageComment.getLastModifiedDate())
                .createdByIdx(storageComment.getCreatedByIdx())
                .lastModifiedByIdx(storageComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storageComment.getIdx())
                .auditBoard("나머지 저장소 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(storageComment.getStorageIdx()))
                .content(storageComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StorageComment storageComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storageComment.getCreatedDate())
                .lastModifiedDate(storageComment.getLastModifiedDate())
                .createdByIdx(storageComment.getCreatedByIdx())
                .lastModifiedByIdx(storageComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storageComment.getIdx())
                .auditBoard("나머지 저장소 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(storageComment.getStorageIdx()))
                .content(storageComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StorageComment storageComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storageComment.getCreatedDate())
                .lastModifiedDate(storageComment.getLastModifiedDate())
                .createdByIdx(storageComment.getCreatedByIdx())
                .lastModifiedByIdx(storageComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storageComment.getIdx())
                .auditBoard("나머지 저장소 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(storageComment.getStorageIdx()))
                .content(storageComment.getContent())
                .build());
    }
}