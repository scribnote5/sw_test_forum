package com.suresoft.sw_test_forum.metric.metric.domain;

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
@Table(name = "metric_attached_file")
public class MetricAttachedFile extends AttachedFileAudit {
    @Column(name = "metric_idx")
    private long metricIdx;

    @Builder
    public MetricAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                              long metricIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.metricIdx = metricIdx;
    }
}
