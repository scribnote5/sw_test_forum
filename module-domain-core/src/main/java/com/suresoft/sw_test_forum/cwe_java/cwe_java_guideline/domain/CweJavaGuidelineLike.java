package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.listener.CweJavaGuidelineLikeListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe_java_guideline_like")
@EntityListeners(CweJavaGuidelineLikeListener.class)
public class CweJavaGuidelineLike extends CommonAudit {
    @Column(name = "cwe_java_guideline_idx")
    private long cweJavaGuidelineIdx;

    @Builder
    public CweJavaGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                long cweJavaGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweJavaGuidelineIdx = cweJavaGuidelineIdx;
    }
}