package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverTool;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class CoverToolListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CoverToolListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CoverTool coverTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverTool.getCreatedDate())
                .lastModifiedDate(coverTool.getLastModifiedDate())
                .createdByIdx(coverTool.getCreatedByIdx())
                .lastModifiedByIdx(coverTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverTool.getIdx())
                .auditBoard("COVER")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(coverTool.getTitle()))
                .content(coverTool.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(CoverTool coverTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverTool.getCreatedDate())
                .lastModifiedDate(coverTool.getLastModifiedDate())
                .createdByIdx(coverTool.getCreatedByIdx())
                .lastModifiedByIdx(coverTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverTool.getIdx())
                .auditBoard("COVER")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(coverTool.getTitle()))
                .content(coverTool.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(CoverTool coverTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(coverTool.getCreatedDate())
                .lastModifiedDate(coverTool.getLastModifiedDate())
                .createdByIdx(coverTool.getCreatedByIdx())
                .lastModifiedByIdx(coverTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(coverTool.getIdx())
                .auditBoard("COVER")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteAuditMessage(coverTool.getTitle()))
                .content(coverTool.getContent())
                .build());
    }
}