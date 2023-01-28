package com.suresoft.sw_test_forum.left_reference.dynamic_test.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.listener.DynamicTestListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "dynamic_test")
@EntityListeners(DynamicTestListener.class)
public class DynamicTest extends CommonAudit {
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
    public DynamicTest(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
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

    public void update(DynamicTest dynamicTest) {
        setActiveStatus(dynamicTest.getActiveStatus());
        this.title = dynamicTest.getTitle();
        this.hashTagsIdx = dynamicTest.getHashTagsIdx();
        this.projectInformationIdx = dynamicTest.getProjectInformationIdx();
        this.guidelineResult = dynamicTest.getGuidelineResult();
        this.guidelineResultNote = dynamicTest.getGuidelineResultNote();
        this.toolInformationIdx = dynamicTest.getToolInformationIdx();
        this.compilerInformationIdx = dynamicTest.getCompilerInformationIdx();
        this.content = dynamicTest.getContent();
        this.nonCompliantExample = dynamicTest.getNonCompliantExample();
        this.compliantExample = dynamicTest.getCompliantExample();
        this.badCasePositionList = dynamicTest.getBadCasePositionList();
        this.goodCasePositionList = dynamicTest.getGoodCasePositionList();
    }
}