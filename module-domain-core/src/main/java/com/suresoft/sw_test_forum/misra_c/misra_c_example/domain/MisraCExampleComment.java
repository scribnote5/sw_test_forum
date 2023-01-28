package com.suresoft.sw_test_forum.misra_c.misra_c_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.listener.MisraCExampleCommentListener;
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
@Table(name = "misra_c_example_comment")
@EntityListeners(MisraCExampleCommentListener.class)
public class MisraCExampleComment extends CommonAudit {
    @Column(name = "misra_c_example_idx")
    private long misraCExampleIdx;

    private String content;

    @Builder
    public MisraCExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                long misraCExampleIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCExampleIdx = misraCExampleIdx;
        this.content = content;
    }

    public void update(MisraCExampleComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}