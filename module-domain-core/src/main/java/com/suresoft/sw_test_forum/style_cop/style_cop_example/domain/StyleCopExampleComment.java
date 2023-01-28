package com.suresoft.sw_test_forum.style_cop.style_cop_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.listener.StyleCopExampleCommentListener;
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
@Table(name = "style_cop_example_comment")
@EntityListeners(StyleCopExampleCommentListener.class)
public class StyleCopExampleComment extends CommonAudit {
    @Column(name = "style_cop_example_idx")
    private long styleCopExampleIdx;

    private String content;

    @Builder
    public StyleCopExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long styleCopExampleIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.styleCopExampleIdx = styleCopExampleIdx;
        this.content = content;
    }

    public void update(StyleCopExampleComment styleCopComment) {
        setActiveStatus(styleCopComment.getActiveStatus());
        this.content = styleCopComment.getContent();
    }
}