package com.suresoft.sw_test_forum.cwe.cwe_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_example.listener.CweExampleCommentListener;
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
@Table(name = "cwe_example_comment")
@EntityListeners(CweExampleCommentListener.class)
public class CweExampleComment extends CommonAudit {
    @Column(name = "cwe_example_idx")
    private long cweExampleIdx;

    private String content;

    @Builder
    public CweExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long cweExampleIdx,
                                  String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweExampleIdx = cweExampleIdx;
        this.content = content;
    }

    public void update(CweExampleComment cweComment) {
        setActiveStatus(cweComment.getActiveStatus());
        this.content = cweComment.getContent();
    }
}