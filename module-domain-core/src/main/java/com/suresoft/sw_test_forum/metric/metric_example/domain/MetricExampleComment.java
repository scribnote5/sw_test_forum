package com.suresoft.sw_test_forum.metric.metric_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_example.listener.MetricExampleCommentListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "metric_example_comment")
@EntityListeners(MetricExampleCommentListener.class)
public class MetricExampleComment extends CommonAudit {
    @Column(name = "metric_example_idx")
    private long metricExampleIdx;

    private String content;

    @Builder
    public MetricExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                long metricExampleIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.metricExampleIdx = metricExampleIdx;
        this.content = content;
    }

    public void update(MetricExampleComment metricComment) {
        setActiveStatus(metricComment.getActiveStatus());
        this.content = metricComment.getContent();
    }
}