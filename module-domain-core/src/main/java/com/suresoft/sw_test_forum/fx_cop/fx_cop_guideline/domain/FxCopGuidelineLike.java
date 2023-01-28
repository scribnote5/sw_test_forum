package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener.FxCopGuidelineLikeListener;
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
@Table(name = "fx_cop_guideline_like")
@EntityListeners(FxCopGuidelineLikeListener.class)
public class FxCopGuidelineLike extends CommonAudit {
    @Column(name = "fx_cop_guideline_idx")
    private long fxCopGuidelineIdx;

    @Builder
    public FxCopGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                 long fxCopGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.fxCopGuidelineIdx = fxCopGuidelineIdx;
    }
}