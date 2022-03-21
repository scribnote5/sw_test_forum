package com.suresoft.sw_test_forum.trouble_shooting.static_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticTool;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class StaticToolListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StaticToolListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StaticTool staticTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticTool.getCreatedDate())
                .lastModifiedDate(staticTool.getLastModifiedDate())
                .createdByIdx(staticTool.getCreatedByIdx())
                .lastModifiedByIdx(staticTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticTool.getIdx())
                .auditBoard("STATIC")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(staticTool.getTitle()))
                .content(staticTool.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(StaticTool staticTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticTool.getCreatedDate())
                .lastModifiedDate(staticTool.getLastModifiedDate())
                .createdByIdx(staticTool.getCreatedByIdx())
                .lastModifiedByIdx(staticTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticTool.getIdx())
                .auditBoard("STATIC")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(staticTool.getTitle()))
                .content(staticTool.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(StaticTool staticTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(staticTool.getCreatedDate())
                .lastModifiedDate(staticTool.getLastModifiedDate())
                .createdByIdx(staticTool.getCreatedByIdx())
                .lastModifiedByIdx(staticTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(staticTool.getIdx())
                .auditBoard("STATIC")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteAuditMessage(staticTool.getTitle()))
                .content(staticTool.getContent())
                .build());
    }
}