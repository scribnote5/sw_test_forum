package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener.MisraCGuidelineLikeListener;
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
@Table(name = "misra_c_guideline_like")
@EntityListeners(MisraCGuidelineLikeListener.class)
public class MisraCGuidelineLike extends CommonAudit {
    @Column(name = "misra_c_guideline_idx")
    private long misraCGuidelineIdx;

    @Builder
    public MisraCGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                               long misraCGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCGuidelineIdx = misraCGuidelineIdx;
    }
}