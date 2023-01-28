package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.listener.ToolTroubleShootingListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tool_trouble_shooting")
@EntityListeners(ToolTroubleShootingListener.class)
public class ToolTroubleShooting extends CommonAudit {
    private String title;

    private long priority;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private ToolCategory toolCategory;

    @Enumerated(EnumType.STRING)
    private TargetToolName targetToolName;

    private long toolInformationIdx;

    private long ideInformationIdx;

    private long compilerInformationIdx;

    private String content;

    @Builder
    public ToolTroubleShooting(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                               String title,
                               long priority,
                               long hashTagsIdx,
                               ToolCategory toolCategory,
                               TargetToolName targetToolName,
                               long toolInformationIdx,
                               long ideInformationIdx,
                               long compilerInformationIdx,
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
        this.toolInformationIdx = toolInformationIdx;
        this.ideInformationIdx = ideInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
    }

    public void update(ToolTroubleShooting toolTroubleShooting) {
        setActiveStatus(toolTroubleShooting.getActiveStatus());
        this.title = toolTroubleShooting.getTitle();
        this.priority = toolTroubleShooting.getPriority();
        this.hashTagsIdx = toolTroubleShooting.getHashTagsIdx();
        this.toolCategory = toolTroubleShooting.getToolCategory();
        this.targetToolName = toolTroubleShooting.getTargetToolName();
        this.toolInformationIdx = toolTroubleShooting.getToolInformationIdx();
        this.ideInformationIdx = toolTroubleShooting.getIdeInformationIdx();
        this.compilerInformationIdx = toolTroubleShooting.getCompilerInformationIdx();
        this.content = toolTroubleShooting.getContent();
    }
}
