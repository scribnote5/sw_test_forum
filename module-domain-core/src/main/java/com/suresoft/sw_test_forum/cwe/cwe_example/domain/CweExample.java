package com.suresoft.sw_test_forum.cwe.cwe_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_example.listener.CweExampleListener;
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
@Table(name = "cwe_example")
@EntityListeners(CweExampleListener.class)
public class CweExample extends CommonAudit {
    @Column(name = "cwe_idx")
    private long cweIdx;

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
    public CweExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                      long cweIdx,
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
        this.cweIdx = cweIdx;
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

    public void update(CweExample cweExample) {
        setActiveStatus(cweExample.getActiveStatus());
        this.cweIdx = cweExample.getCweIdx();
        this.title = cweExample.getTitle();
        this.priority = cweExample.getPriority();
        this.toolInformationIdx = cweExample.getToolInformationIdx();
        this.compilerInformationIdx = cweExample.getCompilerInformationIdx();
        this.content = cweExample.getContent();
        this.nonCompliantExample = cweExample.getNonCompliantExample();
        this.compliantExample = cweExample.getCompliantExample();
        this.badCasePositionList = cweExample.getBadCasePositionList();
        this.goodCasePositionList = cweExample.getGoodCasePositionList();
    }
}
