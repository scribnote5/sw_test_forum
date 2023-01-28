package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExample;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class FxCopExampleListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopExampleListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopExample fxCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExample.getCreatedDate())
                .lastModifiedDate(fxCopExample.getLastModifiedDate())
                .createdByIdx(fxCopExample.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExample.getIdx())
                .auditBoard(".NET Framework Design Guideline 예제")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(fxCopExample.getTitle()))
                .content(fxCopExample.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(FxCopExample fxCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExample.getCreatedDate())
                .lastModifiedDate(fxCopExample.getLastModifiedDate())
                .createdByIdx(fxCopExample.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExample.getIdx())
                .auditBoard(".NET Framework Design Guideline 예제")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(fxCopExample.getTitle()))
                .content(fxCopExample.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(FxCopExample fxCopExample) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopExample.getCreatedDate())
                .lastModifiedDate(fxCopExample.getLastModifiedDate())
                .createdByIdx(fxCopExample.getCreatedByIdx())
                .lastModifiedByIdx(fxCopExample.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopExample.getIdx())
                .auditBoard(".NET Framework Design Guideline 예제")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(fxCopExample.getTitle()))
                .content(fxCopExample.getContent())
                .build());
    }
}
