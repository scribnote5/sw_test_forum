package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.listener.MisraCppCommentListener;
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
@Table(name = "misra_cpp_comment")
@EntityListeners(MisraCppCommentListener.class)
public class MisraCppComment extends CommonAudit {
    @Column(name = "misra_cpp_idx")
    private long misraCppIdx;

    private String content;

    @Builder
    public MisraCppComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long misraCppIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCppIdx = misraCppIdx;
        this.content = content;
    }

    public void update(MisraCppComment misraCppComment) {
        setActiveStatus(misraCppComment.getActiveStatus());
        this.content = misraCppComment.getContent();
    }
}