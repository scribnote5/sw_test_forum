package com.suresoft.sw_test_forum.style_cop.style_cop_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.listener.StyleCopExampleListener;
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
@Table(name = "style_cop_example")
@EntityListeners(StyleCopExampleListener.class)
public class StyleCopExample extends CommonAudit {
    @Column(name = "style_cop_idx")
    private long styleCopIdx;

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
    public StyleCopExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long styleCopIdx,
                           String title,
                           long priority,
                           long toolInformationIdx,
                           long compilerInformationIdx,
                           String content,
                           String nonCompliantExample,
                           String compliantExample,
                           String badCasePositionList,
                           String goodCasePositionList) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.styleCopIdx = styleCopIdx;
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

    public void update(StyleCopExample styleCopExample) {
        setActiveStatus(styleCopExample.getActiveStatus());
        this.styleCopIdx = styleCopExample.getStyleCopIdx();
        this.title = styleCopExample.getTitle();
        this.priority = styleCopExample.getPriority();
        this.toolInformationIdx = styleCopExample.getToolInformationIdx();
        this.compilerInformationIdx = styleCopExample.getCompilerInformationIdx();
        this.content = styleCopExample.getContent();
        this.nonCompliantExample = styleCopExample.getNonCompliantExample();
        this.compliantExample = styleCopExample.getCompliantExample();
        this.badCasePositionList = styleCopExample.getBadCasePositionList();
        this.goodCasePositionList = styleCopExample.getGoodCasePositionList();
    }
}
