package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener.FxCopGuidelineCommentListener;
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
@Table(name = "fx_cop_guideline_comment")
@EntityListeners(FxCopGuidelineCommentListener.class)
public class FxCopGuidelineComment extends CommonAudit {
    @Column(name = "fx_cop_guideline_idx")
    private long fxCopGuidelineIdx;

    private String content;

    @Builder
    public FxCopGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                    long fxCopGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.fxCopGuidelineIdx = fxCopGuidelineIdx;
        this.content = content;
    }

    public void update(FxCopGuidelineComment fxCopComment) {
        setActiveStatus(fxCopComment.getActiveStatus());
        this.content = fxCopComment.getContent();
    }
}