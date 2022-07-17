package com.suresoft.sw_test_forum.cwe_java.cwe_java.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.listener.CweJavaCommentListener;
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
@Table(name = "cwe_java_comment")
@EntityListeners(CweJavaCommentListener.class)
public class CweJavaComment extends CommonAudit {
    @Column(name = "cwe_java_idx")
    private long cweJavaIdx;

    private String content;

    @Builder
    public CweJavaComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                          long cweJavaIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweJavaIdx = cweJavaIdx;
        this.content = content;
    }

    public void update(CweJavaComment cweJavaComment) {
        setActiveStatus(cweJavaComment.getActiveStatus());
        this.content = cweJavaComment.getContent();
    }
}