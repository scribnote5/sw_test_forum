package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener.MisraCGuidelineCommentListener;
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
@Table(name = "misra_c_guideline_comment")
@EntityListeners(MisraCGuidelineCommentListener.class)
public class MisraCGuidelineComment extends CommonAudit {
    @Column(name = "misra_c_guideline_idx")
    private long misraCGuidelineIdx;

    private String content;

    @Builder
    public MisraCGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long misraCGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCGuidelineIdx = misraCGuidelineIdx;
        this.content = content;
    }

    public void update(MisraCGuidelineComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}