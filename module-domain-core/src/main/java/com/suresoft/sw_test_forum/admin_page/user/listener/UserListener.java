package com.suresoft.sw_test_forum.admin_page.user.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class UserListener {
    private final DataHistoryRepository dataHistoryRepository;

    public UserListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(User user) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .createdByIdx(user.getCreatedByIdx())
                .lastModifiedByIdx(user.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(user.getIdx())
                .auditBoard("사용자")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(user.getUsername() + "(" + user.getName() + ")"))
                .content(user.getIntroduction())
                .build());
    }

    @PostUpdate
    public void postUpdate(User user) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .createdByIdx(user.getCreatedByIdx())
                .lastModifiedByIdx(user.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(user.getIdx())
                .auditBoard("사용자")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(user.getUsername() + "(" + user.getName() + ")"))
                .content(user.getIntroduction())
                .build());
    }

    @PostRemove
    public void postRemove(User user) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .createdByIdx(user.getCreatedByIdx())
                .lastModifiedByIdx(user.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(user.getIdx())
                .auditBoard("사용자")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(user.getUsername() + "(" + user.getName() + ")"))
                .content(user.getIntroduction())
                .build());
    }

}
