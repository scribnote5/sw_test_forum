package com.suresoft.sw_test_forum.style_cop.style_cop.domain;

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
@Table(name = "style_cop_attached_file")
public class StyleCopAttachedFile extends AttachedFileAudit {
    @Column(name = "style_cop_idx")
    private long styleCopIdx;

    @Builder
    public StyleCopAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                long styleCopIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.styleCopIdx = styleCopIdx;
    }
}
