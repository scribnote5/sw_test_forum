package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain;

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
@Table(name = "tool_trouble_shooting_comment")
public class ToolTroubleShootingComment extends CommonAudit {
    @Column(name = "tool_trouble_shooting_idx")
    private long toolTroubleShootingIdx;

    private String content;

    @Builder
    public ToolTroubleShootingComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                      long toolTroubleShootingIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.toolTroubleShootingIdx = toolTroubleShootingIdx;
        this.content = content;
    }

    public void update(ToolTroubleShootingComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}