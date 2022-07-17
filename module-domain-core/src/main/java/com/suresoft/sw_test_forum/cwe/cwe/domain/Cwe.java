package com.suresoft.sw_test_forum.cwe.cwe.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.cwe.cwe.listener.CweListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe")
@EntityListeners(CweListener.class)
public class Cwe extends CommonAudit {
    private String title;

    private String originalTitle;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    private String staticTitle;

    private String codeSonarTitle;

    private String content;

    @Builder
    public Cwe(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
               String title,
               String originalTitle,
               long priority,
               Frequency frequency,
               long hashTagsIdx,
               String staticTitle,
               String codeSonarTitle,
               String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.originalTitle = originalTitle;
        this.priority = priority;
        this.frequency = frequency;
        this.hashTagsIdx = hashTagsIdx;
        this.staticTitle = staticTitle;
        this.codeSonarTitle = codeSonarTitle;
        this.content = content;
    }

    public void update(Cwe cwe) {
        setActiveStatus(cwe.getActiveStatus());
        this.title = cwe.getTitle();
        this.originalTitle = cwe.getOriginalTitle();
        this.priority = cwe.getPriority();
        this.frequency = cwe.getFrequency();
        this.hashTagsIdx = cwe.getHashTagsIdx();
        this.staticTitle = cwe.getStaticTitle();
        this.codeSonarTitle = cwe.getCodeSonarTitle();
        this.content = cwe.getContent();
    }
}
