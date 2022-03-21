package com.suresoft.sw_test_forum.metric.metric.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.metric.metric.listener.MetricListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "metric")
@EntityListeners(MetricListener.class)
public class Metric extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    private String content;

    @Builder
    public Metric(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                  String title,
                  long priority,
                  Frequency frequency,
                  long hashTagsIdx,
                  String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.frequency = frequency;
        this.hashTagsIdx = hashTagsIdx;
        this.content = content;
    }

    public void update(Metric metric) {
        setActiveStatus(metric.getActiveStatus());
        this.title = metric.getTitle();
        this.priority = metric.getPriority();
        this.frequency = metric.getFrequency();
        this.hashTagsIdx = metric.getHashTagsIdx();
        this.content = metric.getContent();
    }
}
