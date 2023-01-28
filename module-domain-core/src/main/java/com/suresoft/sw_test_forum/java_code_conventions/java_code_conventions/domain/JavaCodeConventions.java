package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.listener.JavaCodeConventionsListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "java_code_conventions")
@EntityListeners(JavaCodeConventionsListener.class)
public class JavaCodeConventions extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    private String staticTitle;

    private String sparrowTitle;

    private String content;

    @Builder
    public JavaCodeConventions(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                               String title,
                               long priority,
                               Frequency frequency,
                               long hashTagsIdx,
                               String staticTitle,
                               String sparrowTitle,
                               String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.frequency = frequency;
        this.hashTagsIdx = hashTagsIdx;
        this.staticTitle = staticTitle;
        this.sparrowTitle = sparrowTitle;
        this.content = content;
    }

    public void update(JavaCodeConventions javaCodeConventions) {
        setActiveStatus(javaCodeConventions.getActiveStatus());
        this.title = javaCodeConventions.getTitle();
        this.priority = javaCodeConventions.getPriority();
        this.frequency = javaCodeConventions.getFrequency();
        this.hashTagsIdx = javaCodeConventions.getHashTagsIdx();
        this.staticTitle = javaCodeConventions.getStaticTitle();
        this.sparrowTitle = javaCodeConventions.getSparrowTitle();
        this.content = javaCodeConventions.getContent();
    }
}
