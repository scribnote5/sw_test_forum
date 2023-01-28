package com.suresoft.sw_test_forum.tool.tool_configuration.domain;

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
@Table(name = "tool_configuration_attached_file")
public class ToolConfigurationAttachedFile extends AttachedFileAudit {
    @Column(name = "tool_configuration_idx")
    private long toolConfigurationIdx;

    @Builder
    public ToolConfigurationAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                         long toolConfigurationIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.toolConfigurationIdx = toolConfigurationIdx;
    }
}
