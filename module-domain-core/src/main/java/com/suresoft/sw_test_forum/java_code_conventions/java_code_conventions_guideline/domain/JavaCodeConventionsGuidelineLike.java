package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener.JavaCodeConventionsGuidelineLikeListener;
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
@Table(name = "java_code_conventions_guideline_like")
@EntityListeners(JavaCodeConventionsGuidelineLikeListener.class)
public class JavaCodeConventionsGuidelineLike extends CommonAudit {
    @Column(name = "java_code_conventions_guideline_idx")
    private long javaCodeConventionsGuidelineIdx;

    @Builder
    public JavaCodeConventionsGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                            long javaCodeConventionsGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.javaCodeConventionsGuidelineIdx = javaCodeConventionsGuidelineIdx;
    }
}