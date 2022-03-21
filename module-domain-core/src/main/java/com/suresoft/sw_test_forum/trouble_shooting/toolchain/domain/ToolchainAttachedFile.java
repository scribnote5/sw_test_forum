package com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain;

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
@Table(name = "toolchain_attached_file")
public class ToolchainAttachedFile extends AttachedFileAudit {
    @Column(name = "toolchain_idx")
    private long toolchainIdx;

    @Builder
    public ToolchainAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                 long toolchainIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.toolchainIdx = toolchainIdx;
    }
}
