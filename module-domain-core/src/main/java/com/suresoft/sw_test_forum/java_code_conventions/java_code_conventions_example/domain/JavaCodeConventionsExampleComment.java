package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.listener.JavaCodeConventionsExampleCommentListener;
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
@Table(name = "java_code_conventions_example_comment")
@EntityListeners(JavaCodeConventionsExampleCommentListener.class)
public class JavaCodeConventionsExampleComment extends CommonAudit {
    @Column(name = "java_code_conventions_example_idx")
    private long javaCodeConventionsExampleIdx;

    private String content;

    @Builder
    public JavaCodeConventionsExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                             long javaCodeConventionsExampleIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.javaCodeConventionsExampleIdx = javaCodeConventionsExampleIdx;
        this.content = content;
    }

    public void update(JavaCodeConventionsExampleComment javaCodeConventionsComment) {
        setActiveStatus(javaCodeConventionsComment.getActiveStatus());
        this.content = javaCodeConventionsComment.getContent();
    }
}