package com.suresoft.sw_test_forum.cwe.cwe_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class CweGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweGuidelineLike cweGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuidelineLike.getCreatedDate())
                .lastModifiedDate(cweGuidelineLike.getLastModifiedDate())
                .createdByIdx(cweGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(cweGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuidelineLike.getIdx())
                .auditBoard("CWE C/C++ 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(cweGuidelineLike.getCweGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(CweGuidelineLike cweGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweGuidelineLike.getCreatedDate())
                .lastModifiedDate(cweGuidelineLike.getLastModifiedDate())
                .createdByIdx(cweGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(cweGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweGuidelineLike.getIdx())
                .auditBoard("CWE C/C++ 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(cweGuidelineLike.getCweGuidelineIdx()))
                .build());
    }
}