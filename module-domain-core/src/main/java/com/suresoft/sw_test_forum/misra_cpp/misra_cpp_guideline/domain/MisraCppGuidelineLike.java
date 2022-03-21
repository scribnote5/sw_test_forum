package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener.MisraCppGuidelineLikeListener;
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
@Table(name = "misra_cpp_guideline_like")
@EntityListeners(MisraCppGuidelineLikeListener.class)
public class MisraCppGuidelineLike extends CommonAudit {
    @Column(name = "misra_cpp_guideline_idx")
    private long misraCppGuidelineIdx;

    @Builder
    public MisraCppGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                 long misraCppGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCppGuidelineIdx = misraCppGuidelineIdx;
    }
}