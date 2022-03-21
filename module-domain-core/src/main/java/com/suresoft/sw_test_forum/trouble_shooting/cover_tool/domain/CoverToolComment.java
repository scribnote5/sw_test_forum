package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.listener.CoverToolCommentListener;
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
@Table(name = "cover_tool_comment")
@EntityListeners(CoverToolCommentListener.class)
public class CoverToolComment extends CommonAudit {
    @Column(name = "cover_tool_idx")
    private long coverToolIdx;

    private String content;

    @Builder
    public CoverToolComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                            long coverToolIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.coverToolIdx = coverToolIdx;
        this.content = content;
    }

    public void update(CoverToolComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}