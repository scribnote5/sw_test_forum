package com.suresoft.sw_test_forum.metric.metric_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.listener.MetricGuidelineCommentListener;
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
@Table(name = "metric_guideline_comment")
@EntityListeners(MetricGuidelineCommentListener.class)
public class MetricGuidelineComment extends CommonAudit {
    @Column(name = "metric_guideline_idx")
    private long metricGuidelineIdx;

    private String content;

    @Builder
    public MetricGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long metricGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.metricGuidelineIdx = metricGuidelineIdx;
        this.content = content;
    }

    public void update(MetricGuidelineComment metricComment) {
        setActiveStatus(metricComment.getActiveStatus());
        this.content = metricComment.getContent();
    }
}