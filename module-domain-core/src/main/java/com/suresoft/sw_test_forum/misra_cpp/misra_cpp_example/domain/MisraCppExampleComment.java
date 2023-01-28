package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.listener.MisraCppExampleCommentListener;
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
@Table(name = "misra_cpp_example_comment")
@EntityListeners(MisraCppExampleCommentListener.class)
public class MisraCppExampleComment extends CommonAudit {
    @Column(name = "misra_cpp_example_idx")
    private long misraCppExampleIdx;

    private String content;

    @Builder
    public MisraCppExampleComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                  long misraCppExampleIdx,
                                  String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCppExampleIdx = misraCppExampleIdx;
        this.content = content;
    }

    public void update(MisraCppExampleComment misraCppComment) {
        setActiveStatus(misraCppComment.getActiveStatus());
        this.content = misraCppComment.getContent();
    }
}