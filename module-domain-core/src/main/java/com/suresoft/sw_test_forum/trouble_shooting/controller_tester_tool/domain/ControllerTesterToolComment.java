package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "controller_tester_tool_comment")
public class ControllerTesterToolComment extends CommonAudit {
    @Column(name = "controller_tester_tool_idx")
    private long controllerTesterToolIdx;

    private String content;

    @Builder
    public ControllerTesterToolComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                       long controllerTesterToolIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.controllerTesterToolIdx = controllerTesterToolIdx;
        this.content = content;
    }

    public void update(ControllerTesterToolComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}