package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.listener.ControllerTesterToolListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "controller_tester_tool")
@EntityListeners(ControllerTesterToolListener.class)
public class ControllerTesterTool extends CommonAudit {
    private String title;

    private long priority;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private ToolCategory toolCategory;

    private long toolInformationIdx;

    private long ideInformationIdx;

    private long compilerInformationIdx;

    private String content;

    @Builder
    public ControllerTesterTool(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                String title,
                                long priority,
                                long hashTagsIdx,
                                ToolCategory toolCategory,
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
        this.toolInformationIdx = toolInformationIdx;
        this.ideInformationIdx = ideInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
    }

    public void update(ControllerTesterTool staticTool) {
        setActiveStatus(staticTool.getActiveStatus());
        this.title = staticTool.getTitle();
        this.priority = staticTool.getPriority();
        this.hashTagsIdx = staticTool.getHashTagsIdx();
        this.toolCategory = staticTool.getToolCategory();
        this.toolInformationIdx = staticTool.getToolInformationIdx();
        this.ideInformationIdx = staticTool.getIdeInformationIdx();
        this.compilerInformationIdx = staticTool.getCompilerInformationIdx();
        this.content = staticTool.getContent();
    }
}
