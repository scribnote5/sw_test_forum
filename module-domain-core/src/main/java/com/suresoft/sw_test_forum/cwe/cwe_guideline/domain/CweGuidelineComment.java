package com.suresoft.sw_test_forum.cwe.cwe_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.listener.CweGuidelineCommentListener;
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
@Table(name = "cwe_guideline_comment")
@EntityListeners(CweGuidelineCommentListener.class)
public class CweGuidelineComment extends CommonAudit {
    @Column(name = "cwe_guideline_idx")
    private long cweGuidelineIdx;

    private String content;

    @Builder
    public CweGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                    long cweGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweGuidelineIdx = cweGuidelineIdx;
        this.content = content;
    }

    public void update(CweGuidelineComment cweComment) {
        setActiveStatus(cweComment.getActiveStatus());
        this.content = cweComment.getContent();
    }
}