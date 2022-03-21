package com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.listener.ToolchainCommentListener;
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
@Table(name = "toolchain_comment")
@EntityListeners(ToolchainCommentListener.class)
public class ToolchainComment extends CommonAudit {
    @Column(name = "toolchain_idx")
    private long toolchainIdx;

    private String content;

    @Builder
    public ToolchainComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                            long toolchainIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.toolchainIdx = toolchainIdx;
        this.content = content;
    }

    public void update(ToolchainComment misraCComment) {
        setActiveStatus(misraCComment.getActiveStatus());
        this.content = misraCComment.getContent();
    }
}