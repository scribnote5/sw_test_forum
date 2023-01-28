package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShooting;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ToolTroubleShootingListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ToolTroubleShootingListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ToolTroubleShooting toolTroubleShooting) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShooting.getCreatedDate())
                .lastModifiedDate(toolTroubleShooting.getLastModifiedDate())
                .createdByIdx(toolTroubleShooting.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShooting.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShooting.getIdx())
                .auditBoard("도구 트러블슈팅")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(toolTroubleShooting.getTitle()))
                .content(toolTroubleShooting.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ToolTroubleShooting toolTroubleShooting) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShooting.getCreatedDate())
                .lastModifiedDate(toolTroubleShooting.getLastModifiedDate())
                .createdByIdx(toolTroubleShooting.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShooting.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShooting.getIdx())
                .auditBoard("도구 트러블슈팅")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(toolTroubleShooting.getTitle()))
                .content(toolTroubleShooting.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ToolTroubleShooting toolTroubleShooting) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(toolTroubleShooting.getCreatedDate())
                .lastModifiedDate(toolTroubleShooting.getLastModifiedDate())
                .createdByIdx(toolTroubleShooting.getCreatedByIdx())
                .lastModifiedByIdx(toolTroubleShooting.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(toolTroubleShooting.getIdx())
                .auditBoard("도구 트러블슈팅")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(toolTroubleShooting.getTitle()))
                .content(toolTroubleShooting.getContent())
                .build());
    }
}