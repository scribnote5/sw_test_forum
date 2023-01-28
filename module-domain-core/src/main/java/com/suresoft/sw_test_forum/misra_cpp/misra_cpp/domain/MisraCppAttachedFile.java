package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain;

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
@Table(name = "misra_cpp_attached_file")
public class MisraCppAttachedFile extends AttachedFileAudit {
    @Column(name = "misra_cpp_idx")
    private long misraCppIdx;

    @Builder
    public MisraCppAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                long misraCppIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.misraCppIdx = misraCppIdx;
    }
}
