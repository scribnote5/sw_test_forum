package com.suresoft.sw_test_forum.question_answer.listener;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswer;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class QuestionAnswerListener {
    private final DataHistoryRepository dataHistoryRepository;

    public QuestionAnswerListener(@Lazy DataHistoryRepository dataHistoryRepository) {
        this.dataHistoryRepository = dataHistoryRepository;
    }

    @PostPersist
    public void postPersist(QuestionAnswer questionAnswer) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswer.getCreatedDate())
                .lastModifiedDate(questionAnswer.getLastModifiedDate())
                .createdByIdx(questionAnswer.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswer.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswer.getIdx())
                .auditBoard("질문 답변")
                .auditType(AuditType.INSERT)
                .message(AuditMessageUtil.getInsertAuditMessage(questionAnswer.getTitle()))
                .content(questionAnswer.getContent())
                .build());
    }

    @PostUpdate
    public void postUpdate(QuestionAnswer questionAnswer) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswer.getCreatedDate())
                .lastModifiedDate(questionAnswer.getLastModifiedDate())
                .createdByIdx(questionAnswer.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswer.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswer.getIdx())
                .auditBoard("질문 답변")
                .auditType(AuditType.UPDATE)
                .message(AuditMessageUtil.getUpdateAuditMessage(questionAnswer.getTitle()))
                .content(questionAnswer.getContent())
                .build());
    }

    @PostRemove
    public void postRemove(QuestionAnswer questionAnswer) {
        dataHistoryRepository.save(DataHistory.builder()
                .createdDate(questionAnswer.getCreatedDate())
                .lastModifiedDate(questionAnswer.getLastModifiedDate())
                .createdByIdx(questionAnswer.getCreatedByIdx())
                .lastModifiedByIdx(questionAnswer.getLastModifiedByIdx())
                .activeStatus(ActiveStatus.ACTIVE)
                .auditIdx(questionAnswer.getIdx())
                .auditBoard("질문 답변")
                .auditType(AuditType.DELETE)
                .message(AuditMessageUtil.getDeleteAuditMessage(questionAnswer.getTitle()))
                .content(questionAnswer.getContent())
                .build());
    }

}
