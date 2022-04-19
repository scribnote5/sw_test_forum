package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener.StyleCopGuidelineLikeListener;
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
@Table(name = "style_cop_guideline_like")
@EntityListeners(StyleCopGuidelineLikeListener.class)
public class StyleCopGuidelineLike extends CommonAudit {
    @Column(name = "style_cop_guideline_idx")
    private long styleCopGuidelineIdx;

    @Builder
    public StyleCopGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                 long styleCopGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.styleCopGuidelineIdx = styleCopGuidelineIdx;
    }
}