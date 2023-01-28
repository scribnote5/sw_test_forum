package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class FxCopGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public FxCopGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(FxCopGuidelineLike fxCopGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuidelineLike.getCreatedDate())
                .lastModifiedDate(fxCopGuidelineLike.getLastModifiedDate())
                .createdByIdx(fxCopGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuidelineLike.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(fxCopGuidelineLike.getFxCopGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(FxCopGuidelineLike fxCopGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(fxCopGuidelineLike.getCreatedDate())
                .lastModifiedDate(fxCopGuidelineLike.getLastModifiedDate())
                .createdByIdx(fxCopGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(fxCopGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(fxCopGuidelineLike.getIdx())
                .auditBoard(".NET Framework Design Guideline 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(fxCopGuidelineLike.getFxCopGuidelineIdx()))
                .build());
    }
}