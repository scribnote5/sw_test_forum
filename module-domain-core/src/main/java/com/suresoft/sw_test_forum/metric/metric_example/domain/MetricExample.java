package com.suresoft.sw_test_forum.metric.metric_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_example.listener.MetricExampleListener;
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
@Table(name = "metric_example")
@EntityListeners(MetricExampleListener.class)
public class MetricExample extends CommonAudit {
    @Column(name = "metric_idx")
    private long metricIdx;

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
    public MetricExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long metricIdx,
                         String title,
                         long priority,
                         long toolInformationIdx,
                         long compilerInformationIdx,
                         String content,
                         String compliantExample,
                         String nonCompliantExample,
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
        this.priority = priority;
        this.toolInformationIdx = toolInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
        this.nonCompliantExample = nonCompliantExample;
        this.compliantExample = compliantExample;
        this.badCasePositionList = badCasePositionList;
        this.goodCasePositionList = goodCasePositionList;
    }

    public void update(MetricExample metricExample) {
        setActiveStatus(metricExample.getActiveStatus());
        this.metricIdx = metricExample.getMetricIdx();
        this.title = metricExample.getTitle();
        this.priority = metricExample.getPriority();
        this.toolInformationIdx = metricExample.getToolInformationIdx();
        this.compilerInformationIdx = metricExample.getCompilerInformationIdx();
        this.content = metricExample.getContent();
        this.nonCompliantExample = metricExample.getNonCompliantExample();
        this.compliantExample = metricExample.getCompliantExample();
        this.badCasePositionList = metricExample.getBadCasePositionList();
        this.goodCasePositionList = metricExample.getGoodCasePositionList();
    }
}
