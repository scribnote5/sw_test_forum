package com.suresoft.sw_test_forum.cwe_java.cwe_java.domain;

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
@Table(name = "cwe_java_attached_file")
public class CweJavaAttachedFile extends AttachedFileAudit {
    @Column(name = "cwe_java_idx")
    private long cweJavaIdx;

    @Builder
    public CweJavaAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                               long cweJavaIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.cweJavaIdx = cweJavaIdx;
    }
}
