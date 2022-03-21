package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterTool;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControllerTesterToolListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ControllerTesterToolListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ControllerTesterTool controllerTesterTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterTool.getCreatedDate())
                .lastModifiedDate(controllerTesterTool.getLastModifiedDate())
                .createdByIdx(controllerTesterTool.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterTool.getIdx())
                .auditBoard("Controller Tester")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(controllerTesterTool.getTitle()))
                .content(controllerTesterTool.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ControllerTesterTool controllerTesterTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterTool.getCreatedDate())
                .lastModifiedDate(controllerTesterTool.getLastModifiedDate())
                .createdByIdx(controllerTesterTool.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterTool.getIdx())
                .auditBoard("Controller Tester")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(controllerTesterTool.getTitle()))
                .content(controllerTesterTool.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ControllerTesterTool controllerTesterTool) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterTool.getCreatedDate())
                .lastModifiedDate(controllerTesterTool.getLastModifiedDate())
                .createdByIdx(controllerTesterTool.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterTool.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterTool.getIdx())
                .auditBoard("Controller Tester")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteAuditMessage(controllerTesterTool.getTitle()))
                .content(controllerTesterTool.getContent())
                .build());
    }
}