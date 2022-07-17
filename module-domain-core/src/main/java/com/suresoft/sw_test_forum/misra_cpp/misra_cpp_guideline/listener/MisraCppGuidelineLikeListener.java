package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class MisraCppGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public MisraCppGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(MisraCppGuidelineLike misraCppGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuidelineLike.getCreatedDate())
                .lastModifiedDate(misraCppGuidelineLike.getLastModifiedDate())
                .createdByIdx(misraCppGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuidelineLike.getIdx())
                .auditBoard("MISRA C++ 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(misraCppGuidelineLike.getMisraCppGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(MisraCppGuidelineLike misraCppGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(misraCppGuidelineLike.getCreatedDate())
                .lastModifiedDate(misraCppGuidelineLike.getLastModifiedDate())
                .createdByIdx(misraCppGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(misraCppGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(misraCppGuidelineLike.getIdx())
                .auditBoard("MISRA C++ 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(misraCppGuidelineLike.getMisraCppGuidelineIdx()))
                .build());
    }
}