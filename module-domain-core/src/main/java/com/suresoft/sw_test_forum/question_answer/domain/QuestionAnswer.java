package com.suresoft.sw_test_forum.question_answer.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.question_answer.domain.enums.QuestionAnswerType;
import com.suresoft.sw_test_forum.question_answer.listener.QuestionAnswerListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(QuestionAnswerListener.class)
public class QuestionAnswer extends CommonAudit {
    private long groupIdx;

    private String title;

    @Enumerated(EnumType.STRING)
    private QuestionAnswerType type;

    private String content;

    @Builder
    public QuestionAnswer(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                          long groupIdx,
                          String title,
                          QuestionAnswerType type,
                          String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.groupIdx = groupIdx;
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public void update(QuestionAnswer questionAnswer) {
        setActiveStatus(questionAnswer.getActiveStatus());
        this.groupIdx = questionAnswer.getGroupIdx();
        this.title = questionAnswer.getTitle();
        this.type = questionAnswer.getType();
        this.content = questionAnswer.getContent();
    }
}
