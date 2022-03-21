package com.suresoft.sw_test_forum.cwe.cwe.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe.listener.CweCommentListener;
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
@Table(name = "cwe_comment")
@EntityListeners(CweCommentListener.class)
public class CweComment extends CommonAudit {
    @Column(name = "cwe_idx")
    private long cweIdx;

    private String content;

    @Builder
    public CweComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                      long cweIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweIdx = cweIdx;
        this.content = content;
    }

    public void update(CweComment cweComment) {
        setActiveStatus(cweComment.getActiveStatus());
        this.content = cweComment.getContent();
    }
}