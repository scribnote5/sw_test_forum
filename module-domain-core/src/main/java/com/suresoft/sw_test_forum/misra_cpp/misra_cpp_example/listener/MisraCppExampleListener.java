package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCppExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppExample misraCppExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExample.getCreatedDate())
                .lastModifiedDate(misraCppExample.getLastModifiedDate())
                .createdByIdx(misraCppExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExample.getIdx())
                .auditBoard("MISRA C++ 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraCppExample.getTitle()))
                .content(misraCppExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCppExample misraCppExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExample.getCreatedDate())
                .lastModifiedDate(misraCppExample.getLastModifiedDate())
                .createdByIdx(misraCppExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExample.getIdx())
                .auditBoard("MISRA C++ 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraCppExample.getTitle()))
                .content(misraCppExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppExample misraCppExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppExample.getCreatedDate())
                .lastModifiedDate(misraCppExample.getLastModifiedDate())
                .createdByIdx(misraCppExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCppExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppExample.getIdx())
                .auditBoard("MISRA C++ 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraCppExample.getTitle()))
                .content(misraCppExample.getContent())
                .build());
    }
}
