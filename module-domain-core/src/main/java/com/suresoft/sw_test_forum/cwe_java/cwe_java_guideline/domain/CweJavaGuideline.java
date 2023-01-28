package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.listener.CweJavaGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cwe_java_guideline")
@EntityListeners(CweJavaGuidelineListener.class)
public class CweJavaGuideline extends CommonAudit {
    @Column(name = "cwe_java_idx")
    private long cweJavaIdx;

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
    public CweJavaGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                            long cweJavaIdx,
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
        this.cweJavaIdx = cweJavaIdx;
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

    public void update(CweJavaGuideline cweJavaGuideline) {
        setActiveStatus(cweJavaGuideline.getActiveStatus());
        this.cweJavaIdx = cweJavaGuideline.getCweJavaIdx();
        this.title = cweJavaGuideline.getTitle();
        this.hashTagsIdx = cweJavaGuideline.getHashTagsIdx();
        this.projectInformationIdx = cweJavaGuideline.getProjectInformationIdx();
        this.guidelineResult = cweJavaGuideline.getGuidelineResult();
        this.guidelineResultNote = cweJavaGuideline.getGuidelineResultNote();
        this.toolInformationIdx = cweJavaGuideline.getToolInformationIdx();
        this.compilerInformationIdx = cweJavaGuideline.getCompilerInformationIdx();
        this.content = cweJavaGuideline.getContent();
        this.nonCompliantExample = cweJavaGuideline.getNonCompliantExample();
        this.compliantExample = cweJavaGuideline.getCompliantExample();
        this.badCasePositionList = cweJavaGuideline.getBadCasePositionList();
        this.goodCasePositionList = cweJavaGuideline.getGoodCasePositionList();
    }
}