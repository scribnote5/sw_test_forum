package com.suresoft.sw_test_forum.question_answer.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerComment;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class QuestionAnswerCommentListener {
    private final DataHistoryRepository dataHistoryRepository;

    public QuestionAnswerCommentListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(QuestionAnswerComment questionAnswerComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswerComment.getCreatedDate())
                .lastModifiedDate(questionAnswerComment.getLastModifiedDate())
                .createdByIdx(questionAnswerComment.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswerComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswerComment.getIdx())
                .auditBoard("질문 답변 댓글")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertCommentAuditMessage(questionAnswerComment.getQuestionAnswerIdx()))
                .content(questionAnswerComment.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(QuestionAnswerComment questionAnswerComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswerComment.getCreatedDate())
                .lastModifiedDate(questionAnswerComment.getLastModifiedDate())
                .createdByIdx(questionAnswerComment.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswerComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswerComment.getIdx())
                .auditBoard("질문 답변 댓글")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateCommentAuditMessage(questionAnswerComment.getQuestionAnswerIdx()))
                .content(questionAnswerComment.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(QuestionAnswerComment questionAnswerComment) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswerComment.getCreatedDate())
                .lastModifiedDate(questionAnswerComment.getLastModifiedDate())
                .createdByIdx(questionAnswerComment.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswerComment.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswerComment.getIdx())
                .auditBoard("질문 답변 댓글")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteCommentAuditMessage(questionAnswerComment.getQuestionAnswerIdx()))
                .content(questionAnswerComment.getContent())
                .build());
    }
}