package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCGuideline misraCGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuideline.getCreatedDate())
                .lastModifiedDate(misraCGuideline.getLastModifiedDate())
                .createdByIdx(misraCGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuideline.getIdx())
                .auditBoard("MISRA C 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraCGuideline.getTitle()))
                .content(misraCGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCGuideline misraCGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuideline.getCreatedDate())
                .lastModifiedDate(misraCGuideline.getLastModifiedDate())
                .createdByIdx(misraCGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuideline.getIdx())
                .auditBoard("MISRA C 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraCGuideline.getTitle()))
                .content(misraCGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCGuideline misraCGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuideline.getCreatedDate())
                .lastModifiedDate(misraCGuideline.getLastModifiedDate())
                .createdByIdx(misraCGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuideline.getIdx())
                .auditBoard("MISRA C 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraCGuideline.getTitle()))
                .content(misraCGuideline.getContent())
                .build());
    }
}