package com.suresoft.sw_test_forum.tool.tool_usage_method.domain;

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
@Table(name = "tool_usage_method_attached_file")
public class ToolUsageMethodAttachedFile extends AttachedFileAudit {
    @Column(name = "tool_usage_method_idx")
    private long toolUsageMethodIdx;

    @Builder
    public ToolUsageMethodAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                       long toolUsageMethodIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.toolUsageMethodIdx = toolUsageMethodIdx;
    }
}
