package com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.listener.StaticToolCommentListener;
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
@Table(name = "static_tool_comment")
@EntityListeners(StaticToolCommentListener.class)
public class StaticToolComment extends CommonAudit {
    @Column(name = "static_tool_idx")
    private long staticToolIdx;

    private String content;

    @Builder
    public StaticToolComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                             long staticToolIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.staticToolIdx = staticToolIdx;
        this.content = content;
    }

    public void update(StaticToolComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}