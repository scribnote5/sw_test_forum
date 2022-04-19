package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.listener.FxCopExampleCommentListener;
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
@Table(name = "fx_cop_example_comment")
@EntityListeners(FxCopExampleCommentListener.class)
public class FxCopExampleComment extends CommonAudit {
    @Column(name = "fx_cop_example_idx")
    private long fxCopExampleIdx;

    private String content;

    @Builder
    public FxCopExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long fxCopExampleIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.fxCopExampleIdx = fxCopExampleIdx;
        this.content = content;
    }

    public void update(FxCopExampleComment fxCopComment) {
        setActiveStatus(fxCopComment.getActiveStatus());
        this.content = fxCopComment.getContent();
    }
}