package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain;

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
@Table(name = "cwe_java_guideline_attached_file")
public class CweJavaGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "cwe_java_guideline_idx")
    private long cweJavaGuidelineIdx;

    @Builder
    public CweJavaGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                        long cweJavaGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.cweJavaGuidelineIdx = cweJavaGuidelineIdx;
    }
}
