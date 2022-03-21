package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControllerTesterToolCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public ControllerTesterToolCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(ControllerTesterToolComment controllerTesterToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterToolComment.getCreatedDate())
                .lastModifiedDate(controllerTesterToolComment.getLastModifiedDate())
                .createdByIdx(controllerTesterToolComment.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterToolComment.getIdx())
                .auditBoard("Controller Tester 트러블슈팅댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(controllerTesterToolComment.getControllerTesterToolIdx()))
                .content(controllerTesterToolComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(ControllerTesterToolComment controllerTesterToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterToolComment.getCreatedDate())
                .lastModifiedDate(controllerTesterToolComment.getLastModifiedDate())
                .createdByIdx(controllerTesterToolComment.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterToolComment.getIdx())
                .auditBoard("Controller Tester 트러블슈팅댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(controllerTesterToolComment.getControllerTesterToolIdx()))
                .content(controllerTesterToolComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(ControllerTesterToolComment controllerTesterToolComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(controllerTesterToolComment.getCreatedDate())
                .lastModifiedDate(controllerTesterToolComment.getLastModifiedDate())
                .createdByIdx(controllerTesterToolComment.getCreatedByIdx())
                .lastModifiedByIdx(controllerTesterToolComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(controllerTesterToolComment.getIdx())
                .auditBoard("Controller Tester 트러블슈팅댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(controllerTesterToolComment.getControllerTesterToolIdx()))
                .content(controllerTesterToolComment.getContent())
                .build());
    }
}