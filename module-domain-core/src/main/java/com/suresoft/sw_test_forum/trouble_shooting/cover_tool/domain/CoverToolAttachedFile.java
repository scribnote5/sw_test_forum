package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain;

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
@Table(name = "cover_tool_attached_file")
public class CoverToolAttachedFile extends AttachedFileAudit {
    @Column(name = "cover_tool_idx")
    private long coverToolIdx;

    @Builder
    public CoverToolAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                 long coverToolIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.coverToolIdx = coverToolIdx;
    }
}
