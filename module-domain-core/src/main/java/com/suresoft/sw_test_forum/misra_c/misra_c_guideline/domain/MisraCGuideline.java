package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.listener.MisraCGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "misra_c_guideline")
@EntityListeners(MisraCGuidelineListener.class)
public class MisraCGuideline extends CommonAudit {
    @Column(name = "misra_c_idx")
    private long misraCIdx;

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
    public MisraCGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long misraCIdx,
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
        this.misraCIdx = misraCIdx;
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

    public void update(MisraCGuideline misraCGuideline) {
        setActiveStatus(misraCGuideline.getActiveStatus());
        this.misraCIdx = misraCGuideline.getMisraCIdx();
        this.title = misraCGuideline.getTitle();
        this.hashTagsIdx = misraCGuideline.getHashTagsIdx();
        this.projectInformationIdx = misraCGuideline.getProjectInformationIdx();
        this.guidelineResult = misraCGuideline.getGuidelineResult();
        this.guidelineResultNote = misraCGuideline.getGuidelineResultNote();
        this.toolInformationIdx = misraCGuideline.getToolInformationIdx();
        this.compilerInformationIdx = misraCGuideline.getCompilerInformationIdx();
        this.content = misraCGuideline.getContent();
        this.nonCompliantExample = misraCGuideline.getNonCompliantExample();
        this.compliantExample = misraCGuideline.getCompliantExample();
        this.badCasePositionList = misraCGuideline.getBadCasePositionList();
        this.goodCasePositionList = misraCGuideline.getGoodCasePositionList();
    }
}