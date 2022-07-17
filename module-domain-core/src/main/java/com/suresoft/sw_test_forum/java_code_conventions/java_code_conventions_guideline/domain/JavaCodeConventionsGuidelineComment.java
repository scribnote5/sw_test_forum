package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener.JavaCodeConventionsGuidelineCommentListener;
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
@Table(name = "java_code_conventions_guideline_comment")
@EntityListeners(JavaCodeConventionsGuidelineCommentListener.class)
public class JavaCodeConventionsGuidelineComment extends CommonAudit {
    @Column(name = "java_code_conventions_guideline_idx")
    private long javaCodeConventionsGuidelineIdx;

    private String content;

    @Builder
    public JavaCodeConventionsGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                               long javaCodeConventionsGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.javaCodeConventionsGuidelineIdx = javaCodeConventionsGuidelineIdx;
        this.content = content;
    }

    public void update(JavaCodeConventionsGuidelineComment javaCodeConventionsComment) {
        setActiveStatus(javaCodeConventionsComment.getActiveStatus());
        this.content = javaCodeConventionsComment.getContent();
    }
}