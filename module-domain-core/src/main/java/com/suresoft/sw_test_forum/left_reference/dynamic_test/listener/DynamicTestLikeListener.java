package com.suresoft.sw_test_forum.left_reference.dynamic_test.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class DynamicTestLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public DynamicTestLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(DynamicTestLike dynamicTestLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTestLike.getCreatedDate())
                .lastModifiedDate(dynamicTestLike.getLastModifiedDate())
                .createdByIdx(dynamicTestLike.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTestLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTestLike.getIdx())
                .auditBoard("동적시험 미달성 코드 분석 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(dynamicTestLike.getDynamicTestIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(DynamicTestLike dynamicTestLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(dynamicTestLike.getCreatedDate())
                .lastModifiedDate(dynamicTestLike.getLastModifiedDate())
                .createdByIdx(dynamicTestLike.getCreatedByIdx())
                .lastModifiedByIdx(dynamicTestLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(dynamicTestLike.getIdx())
                .auditBoard("동적시험 미달성 코드 분석 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(dynamicTestLike.getDynamicTestIdx()))
                .build());
    }
}