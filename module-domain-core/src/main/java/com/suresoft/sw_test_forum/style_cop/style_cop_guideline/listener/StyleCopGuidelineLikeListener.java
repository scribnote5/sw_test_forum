package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class StyleCopGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public StyleCopGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(StyleCopGuidelineLike styleCopGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuidelineLike.getCreatedDate())
                .lastModifiedDate(styleCopGuidelineLike.getLastModifiedDate())
                .createdByIdx(styleCopGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuidelineLike.getIdx())
                .auditBoard("C# Coding Convention 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(styleCopGuidelineLike.getStyleCopGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(StyleCopGuidelineLike styleCopGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(styleCopGuidelineLike.getCreatedDate())
                .lastModifiedDate(styleCopGuidelineLike.getLastModifiedDate())
                .createdByIdx(styleCopGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(styleCopGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(styleCopGuidelineLike.getIdx())
                .auditBoard("C# Coding Convention 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(styleCopGuidelineLike.getStyleCopGuidelineIdx()))
                .build());
    }
}