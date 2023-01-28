package com.suresoft.sw_test_forum.metric.metric_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.metric.metric_guideline.listener.MetricGuidelineListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "metric_guideline")
@EntityListeners(MetricGuidelineListener.class)
public class MetricGuideline extends CommonAudit {
    @Column(name = "metric_idx")
    private long metricIdx;

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
    public MetricGuideline(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long metricIdx,
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
        this.metricIdx = metricIdx;
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

    public void update(MetricGuideline metricGuideline) {
        setActiveStatus(metricGuideline.getActiveStatus());
        this.metricIdx = metricGuideline.getMetricIdx();
        this.title = metricGuideline.getTitle();
        this.hashTagsIdx = metricGuideline.getHashTagsIdx();
        this.projectInformationIdx = metricGuideline.getProjectInformationIdx();
        this.guidelineResult = metricGuideline.getGuidelineResult();
        this.guidelineResultNote = metricGuideline.getGuidelineResultNote();
        this.toolInformationIdx = metricGuideline.getToolInformationIdx();
        this.compilerInformationIdx = metricGuideline.getCompilerInformationIdx();
        this.content = metricGuideline.getContent();
        this.nonCompliantExample = metricGuideline.getNonCompliantExample();
        this.compliantExample = metricGuideline.getCompliantExample();
        this.badCasePositionList = metricGuideline.getBadCasePositionList();
        this.goodCasePositionList = metricGuideline.getGoodCasePositionList();
    }
}