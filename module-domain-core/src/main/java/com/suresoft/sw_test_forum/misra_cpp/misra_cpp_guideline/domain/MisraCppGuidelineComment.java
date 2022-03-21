package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.listener.MisraCppGuidelineCommentListener;
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
@Table(name = "misra_cpp_guideline_comment")
@EntityListeners(MisraCppGuidelineCommentListener.class)
public class MisraCppGuidelineComment extends CommonAudit {
    @Column(name = "misra_cpp_guideline_idx")
    private long misraCppGuidelineIdx;

    private String content;

    @Builder
    public MisraCppGuidelineComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                    long misraCppGuidelineIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCppGuidelineIdx = misraCppGuidelineIdx;
        this.content = content;
    }

    public void update(MisraCppGuidelineComment misraCppComment) {
        setActiveStatus(misraCppComment.getActiveStatus());
        this.content = misraCppComment.getContent();
    }
}