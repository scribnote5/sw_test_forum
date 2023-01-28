package com.suresoft.sw_test_forum.cwe.cwe_guideline.domain;

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
@Table(name = "cwe_guideline_attached_file")
public class CweGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "cwe_guideline_idx")
    private long cweGuidelineIdx;

    @Builder
    public CweGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                         long cweGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.cweGuidelineIdx = cweGuidelineIdx;
    }
}
