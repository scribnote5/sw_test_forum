package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain;

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
@Table(name = "misra_c_guideline_attached_file")
public class MisraCGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "misra_c_guideline_idx")
    private long misraCGuidelineIdx;

    @Builder
    public MisraCGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                       long misraCGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.misraCGuidelineIdx = misraCGuidelineIdx;
    }
}
