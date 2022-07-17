package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class CweJavaGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public CweJavaGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(CweJavaGuidelineLike cweJavaGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuidelineLike.getCreatedDate())
                .lastModifiedDate(cweJavaGuidelineLike.getLastModifiedDate())
                .createdByIdx(cweJavaGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuidelineLike.getIdx())
                .auditBoard("CWE Java 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(cweJavaGuidelineLike.getCweJavaGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(CweJavaGuidelineLike cweJavaGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(cweJavaGuidelineLike.getCreatedDate())
                .lastModifiedDate(cweJavaGuidelineLike.getLastModifiedDate())
                .createdByIdx(cweJavaGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(cweJavaGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(cweJavaGuidelineLike.getIdx())
                .auditBoard("CWE Java 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(cweJavaGuidelineLike.getCweJavaGuidelineIdx()))
                .build());
    }
}