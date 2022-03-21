package com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain;

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
@Table(name = "static_tool_attached_file")
public class StaticToolAttachedFile extends AttachedFileAudit {
    @Column(name = "static_tool_idx")
    private long staticToolIdx;

    @Builder
    public StaticToolAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                  long staticToolIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.staticToolIdx = staticToolIdx;
    }
}
