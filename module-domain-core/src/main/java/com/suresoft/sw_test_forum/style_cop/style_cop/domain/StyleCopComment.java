package com.suresoft.sw_test_forum.style_cop.style_cop.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop.listener.StyleCopCommentListener;
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
@Table(name = "style_cop_comment")
@EntityListeners(StyleCopCommentListener.class)
public class StyleCopComment extends CommonAudit {
    @Column(name = "style_cop_idx")
    private long styleCopIdx;

    private String content;

    @Builder
    public StyleCopComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long styleCopIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.styleCopIdx = styleCopIdx;
        this.content = content;
    }

    public void update(StyleCopComment styleCopComment) {
        setActiveStatus(styleCopComment.getActiveStatus());
        this.content = styleCopComment.getContent();
    }
}