package com.suresoft.sw_test_forum.tool.tool_usage_method.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.tool.tool_usage_method.listener.ToolUsageMethodListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tool_usage_method")
@EntityListeners(ToolUsageMethodListener.class)
public class ToolUsageMethod extends CommonAudit {
    private String title;

    private long priority;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private ToolCategory toolCategory;

    @Enumerated(EnumType.STRING)
    private TargetToolName targetToolName;

    private String content;

    @Builder
    public ToolUsageMethod(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           String title,
                           long priority,
                           long hashTagsIdx,
                           ToolCategory toolCategory,
                           TargetToolName targetToolName,
                           String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.hashTagsIdx = hashTagsIdx;
        this.toolCategory = toolCategory;
        this.targetToolName = targetToolName;
        this.content = content;
    }

    public void update(ToolUsageMethod toolUsageMethod) {
        setActiveStatus(toolUsageMethod.getActiveStatus());
        this.title = toolUsageMethod.getTitle();
        this.priority = toolUsageMethod.getPriority();
        this.hashTagsIdx = toolUsageMethod.getHashTagsIdx();
        this.toolCategory = toolUsageMethod.getToolCategory();
        this.targetToolName = toolUsageMethod.getTargetToolName();
        this.content = toolUsageMethod.getContent();
    }
}
