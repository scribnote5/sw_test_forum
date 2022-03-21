package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain;

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
@Table(name = "controller_tester_tool_attached_file")
public class ControllerTesterToolAttachedFile extends AttachedFileAudit {
    @Column(name = "controller_tester_tool_idx")
    private long controllerTesterToolIdx;

    @Builder
    public ControllerTesterToolAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                            long controllerTesterToolIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.controllerTesterToolIdx = controllerTesterToolIdx;
    }
}
