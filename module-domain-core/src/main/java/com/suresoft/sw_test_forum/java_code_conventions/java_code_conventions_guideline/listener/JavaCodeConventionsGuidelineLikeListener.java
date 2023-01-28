package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineLike;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class JavaCodeConventionsGuidelineLikeListener {
    private final DataHistoryRepository dataHistoryRepository;

    public JavaCodeConventionsGuidelineLikeListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(JavaCodeConventionsGuidelineLike javaCodeConventionsGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuidelineLike.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuidelineLike.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuidelineLike.getIdx())
                .auditBoard("Java Code Conventions 가이드라인 좋아요")
                .auditType(AuditType.LIKE)
                .message(AuditMessageUtil.getLikeAuditMessage(javaCodeConventionsGuidelineLike.getJavaCodeConventionsGuidelineIdx()))
                .build());
    }

    @PostRemove
    public void postRemove(JavaCodeConventionsGuidelineLike javaCodeConventionsGuidelineLike) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(javaCodeConventionsGuidelineLike.getCreatedDate())
                .lastModifiedDate(javaCodeConventionsGuidelineLike.getLastModifiedDate())
                .createdByIdx(javaCodeConventionsGuidelineLike.getCreatedByIdx())
                .lastModifiedByIdx(javaCodeConventionsGuidelineLike.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(javaCodeConventionsGuidelineLike.getIdx())
                .auditBoard("Java Code Conventions 가이드라인 좋아요")
                .auditType(AuditType.CANCEL_LIKE)
                .message(AuditMessageUtil.getCancelLikeAuditMessage(javaCodeConventionsGuidelineLike.getJavaCodeConventionsGuidelineIdx()))
                .build());
    }
}