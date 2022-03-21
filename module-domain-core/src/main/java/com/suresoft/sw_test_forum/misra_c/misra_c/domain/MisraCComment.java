package com.suresoft.sw_test_forum.misra_c.misra_c.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c.listener.MisraCCommentListener;
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
@Table(name = "misra_c_comment")
@EntityListeners(MisraCCommentListener.class)
public class MisraCComment extends CommonAudit {
    @Column(name = "misra_c_idx")
    private long misraCIdx;

    private String content;

    @Builder
    public MisraCComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long misraCIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCIdx = misraCIdx;
        this.content = content;
    }

    public void update(MisraCComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}