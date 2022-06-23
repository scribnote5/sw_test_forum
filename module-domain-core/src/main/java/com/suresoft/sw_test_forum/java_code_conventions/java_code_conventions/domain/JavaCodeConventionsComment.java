package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.listener.JavaCodeConventionsCommentListener;
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
@Table(name = "java_code_conventions_comment")
@EntityListeners(JavaCodeConventionsCommentListener.class)
public class JavaCodeConventionsComment extends CommonAudit {
    @Column(name = "java_code_conventions_idx")
    private long javaCodeConventionsIdx;

    private String content;

    @Builder
    public JavaCodeConventionsComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                      long javaCodeConventionsIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.javaCodeConventionsIdx = javaCodeConventionsIdx;
        this.content = content;
    }

    public void update(JavaCodeConventionsComment javaCodeConventionsComment) {
        setActiveStatus(javaCodeConventionsComment.getActiveStatus());
        this.content = javaCodeConventionsComment.getContent();
    }
}