package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.listener.JavaCodeConventionsGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "java_code_conventions_guideline")
@EntityListeners(JavaCodeConventionsGuidelineListener.class)
public class JavaCodeConventionsGuideline extends CommonAudit {
    @Column(name = "java_code_conventions_idx")
    private long javaCodeConventionsIdx;

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
    public JavaCodeConventionsGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                        long javaCodeConventionsIdx,
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
        this.javaCodeConventionsIdx = javaCodeConventionsIdx;
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

    public void update(JavaCodeConventionsGuideline javaCodeConventionsGuideline) {
        setActiveStatus(javaCodeConventionsGuideline.getActiveStatus());
        this.javaCodeConventionsIdx = javaCodeConventionsGuideline.getJavaCodeConventionsIdx();
        this.title = javaCodeConventionsGuideline.getTitle();
        this.hashTagsIdx = javaCodeConventionsGuideline.getHashTagsIdx();
        this.projectInformationIdx = javaCodeConventionsGuideline.getProjectInformationIdx();
        this.guidelineResult = javaCodeConventionsGuideline.getGuidelineResult();
        this.guidelineResultNote = javaCodeConventionsGuideline.getGuidelineResultNote();
        this.toolInformationIdx = javaCodeConventionsGuideline.getToolInformationIdx();
        this.compilerInformationIdx = javaCodeConventionsGuideline.getCompilerInformationIdx();
        this.content = javaCodeConventionsGuideline.getContent();
        this.nonCompliantExample = javaCodeConventionsGuideline.getNonCompliantExample();
        this.compliantExample = javaCodeConventionsGuideline.getCompliantExample();
        this.badCasePositionList = javaCodeConventionsGuideline.getBadCasePositionList();
        this.goodCasePositionList = javaCodeConventionsGuideline.getGoodCasePositionList();
    }
}