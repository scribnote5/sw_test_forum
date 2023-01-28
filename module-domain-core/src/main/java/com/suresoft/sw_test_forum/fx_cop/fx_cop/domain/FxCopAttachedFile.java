package com.suresoft.sw_test_forum.fx_cop.fx_cop.domain;

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
@Table(name = "fx_cop_attached_file")
public class FxCopAttachedFile extends AttachedFileAudit {
    @Column(name = "fx_cop_idx")
    private long fxCopIdx;

    @Builder
    public FxCopAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                long fxCopIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.fxCopIdx = fxCopIdx;
    }
}
