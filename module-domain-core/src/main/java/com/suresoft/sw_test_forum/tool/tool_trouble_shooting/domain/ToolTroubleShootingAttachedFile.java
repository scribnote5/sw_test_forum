package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain;

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
@Table(name = "tool_trouble_shooting_attached_file")
public class ToolTroubleShootingAttachedFile extends AttachedFileAudit {
    @Column(name = "tool_trouble_shooting_idx")
    private long toolTroubleShootingIdx;

    @Builder
    public ToolTroubleShootingAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                           long toolTroubleShootingIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.toolTroubleShootingIdx = toolTroubleShootingIdx;
    }
}
