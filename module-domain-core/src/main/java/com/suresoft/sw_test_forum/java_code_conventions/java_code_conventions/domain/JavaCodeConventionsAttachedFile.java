package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain;

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
@Table(name = "java_code_conventions_attached_file")
public class JavaCodeConventionsAttachedFile extends AttachedFileAudit {
    @Column(name = "java_code_conventions_idx")
    private long javaCodeConventionsIdx;

    @Builder
    public JavaCodeConventionsAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                           long javaCodeConventionsIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.javaCodeConventionsIdx = javaCodeConventionsIdx;
    }
}
