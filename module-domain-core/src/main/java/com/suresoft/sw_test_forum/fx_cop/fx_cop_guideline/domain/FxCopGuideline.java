package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.listener.FxCopGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "fx_cop_guideline")
@EntityListeners(FxCopGuidelineListener.class)
public class FxCopGuideline extends CommonAudit {
    @Column(name = "fx_cop_idx")
    private long fxCopIdx;

    private String title;

    private long hashTagsIdx;

    private long projectInformationIdx;

    @Enumerated(EnumType.STRING)
    private GuidelineResult guidelineResult;

    private String guidelineResultNote;

    private long toolInformationIdx;

    private long compilerInformationIdx;

    private String content;

    private String nonCompliantExample;

    private String compliantExample;

    private String badCasePositionList;

    private String goodCasePositionList;

    @Builder
    public FxCopGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                             long fxCopIdx,
                             String title,
                             long hashTagsIdx,
                             long projectInformationIdx,
                             GuidelineResult guidelineResult,
                             String guidelineResultNote,
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
        this.hashTagsIdx = hashTagsIdx;
        this.projectInformationIdx = projectInformationIdx;
        this.guidelineResult = guidelineResult;
        this.guidelineResultNote = guidelineResultNote;
        this.toolInformationIdx = toolInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
        this.nonCompliantExample = nonCompliantExample;
        this.compliantExample = compliantExample;
        this.badCasePositionList = badCasePositionList;
        this.goodCasePositionList = goodCasePositionList;
    }

    public void update(FxCopGuideline fxCopGuideline) {
        setActiveStatus(fxCopGuideline.getActiveStatus());
        this.fxCopIdx = fxCopGuideline.getFxCopIdx();
        this.title = fxCopGuideline.getTitle();
        this.hashTagsIdx = fxCopGuideline.getHashTagsIdx();
        this.projectInformationIdx = fxCopGuideline.getProjectInformationIdx();
        this.guidelineResult = fxCopGuideline.getGuidelineResult();
        this.guidelineResultNote = fxCopGuideline.getGuidelineResultNote();
        this.toolInformationIdx = fxCopGuideline.getToolInformationIdx();
        this.compilerInformationIdx = fxCopGuideline.getCompilerInformationIdx();
        this.content = fxCopGuideline.getContent();
        this.nonCompliantExample = fxCopGuideline.getNonCompliantExample();
        this.compliantExample = fxCopGuideline.getCompliantExample();
        this.badCasePositionList = fxCopGuideline.getBadCasePositionList();
        this.goodCasePositionList = fxCopGuideline.getGoodCasePositionList();
    }
}