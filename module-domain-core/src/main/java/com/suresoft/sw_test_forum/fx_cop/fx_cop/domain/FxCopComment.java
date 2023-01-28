package com.suresoft.sw_test_forum.fx_cop.fx_cop.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.listener.FxCopCommentListener;
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
@Table(name = "fx_cop_comment")
@EntityListeners(FxCopCommentListener.class)
public class FxCopComment extends CommonAudit {
    @Column(name = "fx_cop_idx")
    private long fxCopIdx;

    private String content;

    @Builder
    public FxCopComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long fxCopIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.fxCopIdx = fxCopIdx;
        this.content = content;
    }

    public void update(FxCopComment fxCopComment) {
        setActiveStatus(fxCopComment.getActiveStatus());
        this.content = fxCopComment.getContent();
    }
}