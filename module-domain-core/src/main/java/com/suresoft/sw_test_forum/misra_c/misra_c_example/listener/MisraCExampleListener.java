package com.suresoft.sw_test_forum.misra_c.misra_c_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class MisraCExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCExample misraCExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExample.getCreatedDate())
                .lastModifiedDate(misraCExample.getLastModifiedDate())
                .createdByIdx(misraCExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExample.getIdx())
                .auditBoard("MISRA C 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(misraCExample.getTitle()))
                .content(misraCExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(MisraCExample misraCExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExample.getCreatedDate())
                .lastModifiedDate(misraCExample.getLastModifiedDate())
                .createdByIdx(misraCExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExample.getIdx())
                .auditBoard("MISRA C 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(misraCExample.getTitle()))
                .content(misraCExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(MisraCExample misraCExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCExample.getCreatedDate())
                .lastModifiedDate(misraCExample.getLastModifiedDate())
                .createdByIdx(misraCExample.getCreatedByIdx())
                .lastModifiedByIdx(misraCExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCExample.getIdx())
                .auditBoard("MISRA C 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(misraCExample.getTitle()))
                .content(misraCExample.getContent())
                .build());
    }
}
