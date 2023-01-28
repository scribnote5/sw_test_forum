package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.listener.CweJavaExampleCommentListener;
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
@Table(name = "cwe_java_example_comment")
@EntityListeners(CweJavaExampleCommentListener.class)
public class CweJavaExampleComment extends CommonAudit {
    @Column(name = "cwe_java_example_idx")
    private long cweJavaExampleIdx;

    private String content;

    @Builder
    public CweJavaExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                 long cweJavaExampleIdx,
                                 String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweJavaExampleIdx = cweJavaExampleIdx;
        this.content = content;
    }

    public void update(CweJavaExampleComment cweJavaComment) {
        setActiveStatus(cweJavaComment.getActiveStatus());
        this.content = cweJavaComment.getContent();
    }
}