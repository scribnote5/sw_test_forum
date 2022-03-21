package com.suresoft.sw_test_forum.cwe.cwe_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.listener.CweGuidelineLikeListener;
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
@Table(name = "cwe_guideline_like")
@EntityListeners(CweGuidelineLikeListener.class)
public class CweGuidelineLike extends CommonAudit {
    @Column(name = "cwe_guideline_idx")
    private long cweGuidelineIdx;

    @Builder
    public CweGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                            long cweGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweGuidelineIdx = cweGuidelineIdx;
    }
}