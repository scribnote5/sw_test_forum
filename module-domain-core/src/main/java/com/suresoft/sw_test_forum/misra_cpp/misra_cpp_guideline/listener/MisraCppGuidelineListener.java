package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppGuidelineListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppGuidelineListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppGuideline misraCppGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuideline.getCreatedDate())
                .lastModifiedDate(misraCppGuideline.getLastModifiedDate())
                .createdByIdx(misraCppGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuideline.getIdx())
                .auditBoard("MISRA C++ 가이드라인")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraCppGuideline.getTitle()))
                .content(misraCppGuideline.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCppGuideline misraCppGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuideline.getCreatedDate())
                .lastModifiedDate(misraCppGuideline.getLastModifiedDate())
                .createdByIdx(misraCppGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuideline.getIdx())
                .auditBoard("MISRA C++ 가이드라인")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraCppGuideline.getTitle()))
                .content(misraCppGuideline.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppGuideline misraCppGuideline) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuideline.getCreatedDate())
                .lastModifiedDate(misraCppGuideline.getLastModifiedDate())
                .createdByIdx(misraCppGuideline.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuideline.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuideline.getIdx())
                .auditBoard("MISRA C++ 가이드라인")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraCppGuideline.getTitle()))
                .content(misraCppGuideline.getContent())
                .build());
    }

}
