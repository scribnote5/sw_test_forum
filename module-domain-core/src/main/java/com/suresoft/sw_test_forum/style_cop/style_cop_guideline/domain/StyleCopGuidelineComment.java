package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener.StyleCopGuidelineCommentListener;
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
@Table(name = "style_cop_guideline_comment")
@EntityListeners(StyleCopGuidelineCommentListener.class)
public class StyleCopGuidelineComment extends CommonAudit {
    @Column(name = "style_cop_guideline_idx")
    private long styleCopGuidelineIdx;

    private String content;

    @Builder
    public StyleCopGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                    long styleCopGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.styleCopGuidelineIdx = styleCopGuidelineIdx;
        this.content = content;
    }

    public void update(StyleCopGuidelineComment styleCopComment) {
        setActiveStatus(styleCopComment.getActiveStatus());
        this.content = styleCopComment.getContent();
    }
}