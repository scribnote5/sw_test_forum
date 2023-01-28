package com.suresoft.sw_test_forum.metric.metric_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.listener.MetricGuidelineLikeListener;
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
@Table(name = "metric_guideline_like")
@EntityListeners(MetricGuidelineLikeListener.class)
public class MetricGuidelineLike extends CommonAudit {
    @Column(name = "metric_guideline_idx")
    private long metricGuidelineIdx;

    @Builder
    public MetricGuidelineLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                               long metricGuidelineIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.metricGuidelineIdx = metricGuidelineIdx;
    }
}