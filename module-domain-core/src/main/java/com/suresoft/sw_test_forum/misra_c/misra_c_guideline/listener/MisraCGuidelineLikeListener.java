package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class MisraCGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCGuidelineLike misraCGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuidelineLike.getCreatedDate())
                .lastModifiedDate(misraCGuidelineLike.getLastModifiedDate())
                .createdByIdx(misraCGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuidelineLike.getIdx())
                .auditBoard("MISRA C 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(misraCGuidelineLike.getMisraCGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(MisraCGuidelineLike misraCGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCGuidelineLike.getCreatedDate())
                .lastModifiedDate(misraCGuidelineLike.getLastModifiedDate())
                .createdByIdx(misraCGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(misraCGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCGuidelineLike.getIdx())
                .auditBoard("MISRA C 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(misraCGuidelineLike.getMisraCGuidelineIdx()))
                .build());
    }
}