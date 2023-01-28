package com.suresoft.sw_test_forum.question_answer.domain;

import com.suresoft.sw_test_forum.common.domain.AttachedFileAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table
public class QuestionAnswerAttachedFile extends AttachedFileAudit {
    private long questionAnswerIdx;

    @Builder
    public QuestionAnswerAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                      long questionAnswerIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.questionAnswerIdx = questionAnswerIdx;
    }
}
