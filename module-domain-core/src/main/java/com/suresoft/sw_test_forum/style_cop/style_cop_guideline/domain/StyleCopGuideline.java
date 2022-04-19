package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.listener.StyleCopGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "style_cop_guideline")
@EntityListeners(StyleCopGuidelineListener.class)
public class StyleCopGuideline extends CommonAudit {
    @Column(name = "style_cop_idx")
    private long styleCopIdx;

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
    public StyleCopGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                             long styleCopIdx,
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
        this.styleCopIdx = styleCopIdx;
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

    public void update(StyleCopGuideline styleCopGuideline) {
        setActiveStatus(styleCopGuideline.getActiveStatus());
        this.styleCopIdx = styleCopGuideline.getStyleCopIdx();
        this.title = styleCopGuideline.getTitle();
        this.hashTagsIdx = styleCopGuideline.getHashTagsIdx();
        this.projectInformationIdx = styleCopGuideline.getProjectInformationIdx();
        this.guidelineResult = styleCopGuideline.getGuidelineResult();
        this.guidelineResultNote = styleCopGuideline.getGuidelineResultNote();
        this.toolInformationIdx = styleCopGuideline.getToolInformationIdx();
        this.compilerInformationIdx = styleCopGuideline.getCompilerInformationIdx();
        this.content = styleCopGuideline.getContent();
        this.nonCompliantExample = styleCopGuideline.getNonCompliantExample();
        this.compliantExample = styleCopGuideline.getCompliantExample();
        this.badCasePositionList = styleCopGuideline.getBadCasePositionList();
        this.goodCasePositionList = styleCopGuideline.getGoodCasePositionList();
    }
}