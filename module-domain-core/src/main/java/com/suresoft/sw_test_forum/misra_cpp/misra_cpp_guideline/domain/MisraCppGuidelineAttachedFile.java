package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain;

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
@Table(name = "misra_cpp_guideline_attached_file")
public class MisraCppGuidelineAttachedFile extends AttachedFileAudit {
    @Column(name = "misra_cpp_guideline_idx")
    private long misraCppGuidelineIdx;

    @Builder
    public MisraCppGuidelineAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                         long misraCppGuidelineIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.misraCppGuidelineIdx = misraCppGuidelineIdx;
    }
}
