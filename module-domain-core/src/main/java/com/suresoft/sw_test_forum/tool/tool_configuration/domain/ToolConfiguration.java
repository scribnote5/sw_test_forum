package com.suresoft.sw_test_forum.tool.tool_configuration.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.OperationEnvironment;
import com.suresoft.sw_test_forum.common.domain.enums.ProgrammingLanguage;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.tool.tool_configuration.listener.ToolConfigurationListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tool_configuration")
@EntityListeners(ToolConfigurationListener.class)
public class ToolConfiguration extends CommonAudit {
    private String title;

    private long priority;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private TargetToolName targetToolName;

    private long toolInformationIdx;

    private long developmentEnvironmentInformationIdx;

    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage programmingLanguage;

    private long ideInformationIdx;

    private long compilerInformationIdx;

    @Enumerated(EnumType.STRING)
    private OperationEnvironment operationEnvironment;

    private long targetEnvironmentInformationIdx;

    private long targetInformationIdx;

    private String content;

    @Builder
    public ToolConfiguration(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                             String title,
                             long priority,
                             long hashTagsIdx,
                             TargetToolName targetToolName,
                             long toolInformationIdx,
                             long developmentEnvironmentInformationIdx,
                             ProgrammingLanguage programmingLanguage,
                             long ideInformationIdx,
                             long compilerInformationIdx,
                             OperationEnvironment operationEnvironment,
                             long targetEnvironmentInformationIdx,
                             long targetInformationIdx,
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
        this.targetToolName = targetToolName;
        this.toolInformationIdx = toolInformationIdx;
        this.developmentEnvironmentInformationIdx = developmentEnvironmentInformationIdx;
        this.programmingLanguage = programmingLanguage;
        this.ideInformationIdx = ideInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.operationEnvironment = operationEnvironment;
        this.targetEnvironmentInformationIdx = targetEnvironmentInformationIdx;
        this.targetInformationIdx = targetInformationIdx;
        this.content = content;
    }

    public void update(ToolConfiguration toolConfiguration) {
        setActiveStatus(toolConfiguration.getActiveStatus());
        this.title = toolConfiguration.getTitle();
        this.priority = toolConfiguration.getPriority();
        this.hashTagsIdx = toolConfiguration.getHashTagsIdx();
        this.targetToolName = toolConfiguration.getTargetToolName();
        this.toolInformationIdx = toolConfiguration.getToolInformationIdx();
        this.developmentEnvironmentInformationIdx = toolConfiguration.getDevelopmentEnvironmentInformationIdx();
        this.programmingLanguage = toolConfiguration.getProgrammingLanguage();
        this.ideInformationIdx = toolConfiguration.getIdeInformationIdx();
        this.compilerInformationIdx = toolConfiguration.getCompilerInformationIdx();
        this.operationEnvironment = toolConfiguration.getOperationEnvironment();
        this.targetEnvironmentInformationIdx = toolConfiguration.getTargetEnvironmentInformationIdx();
        this.targetInformationIdx = toolConfiguration.getTargetInformationIdx();
        this.content = toolConfiguration.getContent();
    }
}
