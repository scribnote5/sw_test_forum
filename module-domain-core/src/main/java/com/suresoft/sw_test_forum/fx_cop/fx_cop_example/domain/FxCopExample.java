package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.listener.FxCopExampleListener;
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
@Table(name = "fx_cop_example")
@EntityListeners(FxCopExampleListener.class)
public class FxCopExample extends CommonAudit {
    @Column(name = "fx_cop_idx")
    private long fxCopIdx;

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
    public FxCopExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long fxCopIdx,
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
        this.fxCopIdx = fxCopIdx;
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

    public void update(FxCopExample fxCopExample) {
        setActiveStatus(fxCopExample.getActiveStatus());
        this.fxCopIdx = fxCopExample.getFxCopIdx();
        this.title = fxCopExample.getTitle();
        this.priority = fxCopExample.getPriority();
        this.toolInformationIdx = fxCopExample.getToolInformationIdx();
        this.compilerInformationIdx = fxCopExample.getCompilerInformationIdx();
        this.content = fxCopExample.getContent();
        this.nonCompliantExample = fxCopExample.getNonCompliantExample();
        this.compliantExample = fxCopExample.getCompliantExample();
        this.badCasePositionList = fxCopExample.getBadCasePositionList();
        this.goodCasePositionList = fxCopExample.getGoodCasePositionList();
    }
}
