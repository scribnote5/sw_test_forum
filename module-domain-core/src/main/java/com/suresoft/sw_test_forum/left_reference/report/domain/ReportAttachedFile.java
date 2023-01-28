package com.suresoft.sw_test_forum.left_reference.report.domain;

import com.suresoft.sw_test_forum.common.domain.AttachedFileAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table
public class ReportAttachedFile extends AttachedFileAudit {
    private long reportIdx;

    @Builder
    public ReportAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                              long reportIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.reportIdx = reportIdx;
    }
}
