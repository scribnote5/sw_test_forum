package com.suresoft.sw_test_forum.cwe_java.cwe_java.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.listener.CweJavaListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe_java")
@EntityListeners(CweJavaListener.class)
public class CweJava extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    private String staticTitle;

    private String sparrowTitle;

    private String content;

    @Builder
    public CweJava(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
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

    public void update(CweJava cweJava) {
        setActiveStatus(cweJava.getActiveStatus());
        this.title = cweJava.getTitle();
        this.priority = cweJava.getPriority();
        this.frequency = cweJava.getFrequency();
        this.hashTagsIdx = cweJava.getHashTagsIdx();
        this.staticTitle = cweJava.getStaticTitle();
        this.sparrowTitle = cweJava.getSparrowTitle();
        this.content = cweJava.getContent();
    }
}
