package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain;

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
@Table(name = "style_cop_guideline_attached_file")
public class StyleCopGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "style_cop_guideline_idx")
    private long styleCopGuidelineIdx;

    @Builder
    public StyleCopGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                         long styleCopGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.styleCopGuidelineIdx = styleCopGuidelineIdx;
    }
}
