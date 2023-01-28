package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.listener.CweJavaExampleListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe_java_example")
@EntityListeners(CweJavaExampleListener.class)
public class CweJavaExample extends CommonAudit {
    @Column(name = "cwe_java_idx")
    private long cweJavaIdx;

    private String title;

    private long priority;

    private long toolInformationIdx;

    private long compilerInformationIdx;

    private String content;

    private String nonCompliantExample;

    private String compliantExample;

    private String badCasePositionList;

    private String goodCasePositionList;

    @Builder
    public CweJavaExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                          long cweJavaIdx,
                          String title,
                          long priority,
                          long toolInformationIdx,
                          long compilerInformationIdx,
                          String content,
                          String compliantExample,
                          String nonCompliantExample,
                          String badCasePositionList,
                          String goodCasePositionList) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.cweJavaIdx = cweJavaIdx;
        this.title = title;
        this.priority = priority;
        this.toolInformationIdx = toolInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
        this.nonCompliantExample = nonCompliantExample;
        this.compliantExample = compliantExample;
        this.badCasePositionList = badCasePositionList;
        this.goodCasePositionList = goodCasePositionList;
    }

    public void update(CweJavaExample cweJavaExample) {
        setActiveStatus(cweJavaExample.getActiveStatus());
        this.cweJavaIdx = cweJavaExample.getCweJavaIdx();
        this.title = cweJavaExample.getTitle();
        this.priority = cweJavaExample.getPriority();
        this.toolInformationIdx = cweJavaExample.getToolInformationIdx();
        this.compilerInformationIdx = cweJavaExample.getCompilerInformationIdx();
        this.content = cweJavaExample.getContent();
        this.nonCompliantExample = cweJavaExample.getNonCompliantExample();
        this.compliantExample = cweJavaExample.getCompliantExample();
        this.badCasePositionList = cweJavaExample.getBadCasePositionList();
        this.goodCasePositionList = cweJavaExample.getGoodCasePositionList();
    }
}
