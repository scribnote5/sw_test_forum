package com.suresoft.sw_test_forum.tool.tool_usage_method.domain;

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
@Table(name = "tool_usage_method_comment")
public class ToolUsageMethodComment extends CommonAudit {
    @Column(name = "tool_usage_method_idx")
    private long toolUsageMethodIdx;

    private String content;

    @Builder
    public ToolUsageMethodComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long toolUsageMethodIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.toolUsageMethodIdx = toolUsageMethodIdx;
        this.content = content;
    }

    public void update(ToolUsageMethodComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}