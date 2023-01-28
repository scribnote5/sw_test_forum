package com.suresoft.sw_test_forum.left_reference.storage.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.storage.domain.Storage;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StorageListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StorageListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Storage storage) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storage.getCreatedDate())
                .lastModifiedDate(storage.getLastModifiedDate())
                .createdByIdx(storage.getCreatedByIdx())
                .lastModifiedByIdx(storage.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storage.getIdx())
                .auditBoard("나머지 저장소")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(storage.getTitle()))
                .content(storage.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Storage storage) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storage.getCreatedDate())
                .lastModifiedDate(storage.getLastModifiedDate())
                .createdByIdx(storage.getCreatedByIdx())
                .lastModifiedByIdx(storage.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storage.getIdx())
                .auditBoard("나머지 저장소")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(storage.getTitle()))
                .content(storage.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Storage storage) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(storage.getCreatedDate())
                .lastModifiedDate(storage.getLastModifiedDate())
                .createdByIdx(storage.getCreatedByIdx())
                .lastModifiedByIdx(storage.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(storage.getIdx())
                .auditBoard("나머지 저장소")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(storage.getTitle()))
                .content(storage.getContent())
                .build());
    }
    
}
