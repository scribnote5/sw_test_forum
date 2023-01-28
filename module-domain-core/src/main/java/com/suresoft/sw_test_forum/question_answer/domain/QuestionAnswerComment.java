package com.suresoft.sw_test_forum.question_answer.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.question_answer.listener.QuestionAnswerCommentListener;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(QuestionAnswerCommentListener.class)
public class QuestionAnswerComment extends CommonAudit {
    private long questionAnswerIdx;

    private String content;

    @Builder
    public QuestionAnswerComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                 long questionAnswerIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.questionAnswerIdx = questionAnswerIdx;
        this.content = content;
    }

    public void update(QuestionAnswerComment questionAnswerComment) {
        setActiveStatus(questionAnswerComment.getActiveStatus());
        this.content = questionAnswerComment.getContent();
    }
}