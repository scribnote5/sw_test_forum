package com.suresoft.sw_test_forum.metric.metric.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric.listener.MetricCommentListener;
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
@Table(name = "metric_comment")
@EntityListeners(MetricCommentListener.class)
public class MetricComment extends CommonAudit {
    @Column(name = "metric_idx")
    private long metricIdx;

    private String content;

    @Builder
    public MetricComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long metricIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.metricIdx = metricIdx;
        this.content = content;
    }

    public void update(MetricComment metricComment) {
        setActiveStatus(metricComment.getActiveStatus());
        this.content = metricComment.getContent();
    }
}