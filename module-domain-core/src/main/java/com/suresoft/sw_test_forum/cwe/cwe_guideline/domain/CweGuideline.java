package com.suresoft.sw_test_forum.cwe.cwe_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.listener.CweGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe_guideline")
@EntityListeners(CweGuidelineListener.class)
public class CweGuideline extends CommonAudit {
    @Column(name = "cwe_idx")
    private long cweIdx;

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
    public CweGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                        long cweIdx,
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
        this.cweIdx = cweIdx;
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

    public void update(CweGuideline cweGuideline) {
        setActiveStatus(cweGuideline.getActiveStatus());
        this.cweIdx = cweGuideline.getCweIdx();
        this.title = cweGuideline.getTitle();
        this.hashTagsIdx = cweGuideline.getHashTagsIdx();
        this.projectInformationIdx = cweGuideline.getProjectInformationIdx();
        this.guidelineResult = cweGuideline.getGuidelineResult();
        this.guidelineResultNote = cweGuideline.getGuidelineResultNote();
        this.toolInformationIdx = cweGuideline.getToolInformationIdx();
        this.compilerInformationIdx = cweGuideline.getCompilerInformationIdx();
        this.content = cweGuideline.getContent();
        this.nonCompliantExample = cweGuideline.getNonCompliantExample();
        this.compliantExample = cweGuideline.getCompliantExample();
        this.badCasePositionList = cweGuideline.getBadCasePositionList();
        this.goodCasePositionList = cweGuideline.getGoodCasePositionList();
    }
}