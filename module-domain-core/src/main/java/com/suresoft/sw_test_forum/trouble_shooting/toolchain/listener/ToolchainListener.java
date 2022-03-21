package com.suresoft.sw_test_forum.trouble_shooting.toolchain.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.Toolchain;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolchainListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolchainListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(Toolchain toolchain) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchain.getCreatedDate())
                .lastModifiedDate(toolchain.getLastModifiedDate())
                .createdByIdx(toolchain.getCreatedByIdx())
                .lastModifiedByIdx(toolchain.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchain.getIdx())
                .auditBoard("툴체인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(toolchain.getTitle()))
                .content(toolchain.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(Toolchain toolchain) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchain.getCreatedDate())
                .lastModifiedDate(toolchain.getLastModifiedDate())
                .createdByIdx(toolchain.getCreatedByIdx())
                .lastModifiedByIdx(toolchain.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchain.getIdx())
                .auditBoard("툴체인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(toolchain.getTitle()))
                .content(toolchain.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(Toolchain toolchain) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolchain.getCreatedDate())
                .lastModifiedDate(toolchain.getLastModifiedDate())
                .createdByIdx(toolchain.getCreatedByIdx())
                .lastModifiedByIdx(toolchain.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolchain.getIdx())
                .auditBoard("툴체인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteAuditMessage(toolchain.getTitle()))
                .content(toolchain.getContent())
                .build());
    }
}