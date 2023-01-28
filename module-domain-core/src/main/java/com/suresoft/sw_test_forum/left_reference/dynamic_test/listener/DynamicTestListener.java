package com.suresoft.sw_test_forum.left_reference.dynamic_test.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTest;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class DynamicTestListener {
    private final DataHistoryRepository dataHistoryRepository;

    public DynamicTestListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(DynamicTest dynamicTest) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTest.getCreatedDate())
                .lastModifiedDate(dynamicTest.getLastModifiedDate())
                .createdByIdx(dynamicTest.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTest.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTest.getIdx())
                .auditBoard("동적시험 미달성 코드 분석")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(dynamicTest.getTitle()))
                .content(dynamicTest.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(DynamicTest dynamicTest) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTest.getCreatedDate())
                .lastModifiedDate(dynamicTest.getLastModifiedDate())
                .createdByIdx(dynamicTest.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTest.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTest.getIdx())
                .auditBoard("동적시험 미달성 코드 분석")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(dynamicTest.getTitle()))
                .content(dynamicTest.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(DynamicTest dynamicTest) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTest.getCreatedDate())
                .lastModifiedDate(dynamicTest.getLastModifiedDate())
                .createdByIdx(dynamicTest.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTest.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTest.getIdx())
                .auditBoard("동적시험 미달성 코드 분석")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(dynamicTest.getTitle()))
                .content(dynamicTest.getContent())
                .build());
    }
}