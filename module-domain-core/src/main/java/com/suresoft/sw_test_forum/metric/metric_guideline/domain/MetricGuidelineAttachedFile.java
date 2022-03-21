package com.suresoft.sw_test_forum.metric.metric_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.AttachedFileAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "metric_guideline_attached_file")
public class MetricGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "metric_guideline_idx")
    private long metricGuidelineIdx;

    @Builder
    public MetricGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                       long metricGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.metricGuidelineIdx = metricGuidelineIdx;
    }
}
