package com.suresoft.sw_test_forum.tool.tool_configuration.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_configuration.listener.ToolConfigurationCommentListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tool_configuration_comment")
@EntityListeners(ToolConfigurationCommentListener.class)
public class ToolConfigurationComment extends CommonAudit {
    @Column(name = "tool_configuration_idx")
    private long toolConfigurationIdx;

    private String content;

    @Builder
    public ToolConfigurationComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                    long toolConfigurationIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.toolConfigurationIdx = toolConfigurationIdx;
        this.content = content;
    }

    public void update(ToolConfigurationComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}