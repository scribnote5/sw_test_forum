package com.suresoft.sw_test_forum.misra_c.misra_c.domain;

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
@Table(name = "misra_c_attached_file")
public class MisraCAttachedFile extends AttachedFileAudit {
    @Column(name = "misra_c_idx")
    private long misraCIdx;

    @Builder
    public MisraCAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                              long misraCIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.misraCIdx = misraCIdx;
    }
}
