package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener.MisraCppGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "misra_cpp_guideline")
@EntityListeners(MisraCppGuidelineListener.class)
public class MisraCppGuideline extends CommonAudit {
    @Column(name = "misra_cpp_idx")
    private long misraCppIdx;

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
    public MisraCppGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                             long misraCppIdx,
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
        this.misraCppIdx = misraCppIdx;
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

    public void update(MisraCppGuideline misraCppGuideline) {
        setActiveStatus(misraCppGuideline.getActiveStatus());
        this.misraCppIdx = misraCppGuideline.getMisraCppIdx();
        this.title = misraCppGuideline.getTitle();
        this.hashTagsIdx = misraCppGuideline.getHashTagsIdx();
        this.projectInformationIdx = misraCppGuideline.getProjectInformationIdx();
        this.guidelineResult = misraCppGuideline.getGuidelineResult();
        this.guidelineResultNote = misraCppGuideline.getGuidelineResultNote();
        this.toolInformationIdx = misraCppGuideline.getToolInformationIdx();
        this.compilerInformationIdx = misraCppGuideline.getCompilerInformationIdx();
        this.content = misraCppGuideline.getContent();
        this.nonCompliantExample = misraCppGuideline.getNonCompliantExample();
        this.compliantExample = misraCppGuideline.getCompliantExample();
        this.badCasePositionList = misraCppGuideline.getBadCasePositionList();
        this.goodCasePositionList = misraCppGuideline.getGoodCasePositionList();
    }
}