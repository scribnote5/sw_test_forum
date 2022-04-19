package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain;

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
@Table(name = "fx_cop_guideline_attached_file")
public class FxCopGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "fx_cop_guideline_idx")
    private long fxCopGuidelineIdx;

    @Builder
    public FxCopGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                         long fxCopGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.fxCopGuidelineIdx = fxCopGuidelineIdx;
    }
}
