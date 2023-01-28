package com.suresoft.sw_test_forum.question_answer.repository;

import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerCommentRepository extends JpaRepository<QuestionAnswerComment, Long> {
    /**
     * 리스트 조회
     *
     * @param questionAnswerIdx
     * @return
     */
    List<QuestionAnswerComment> findAllByQuestionAnswerIdxOrderByIdxDesc(long questionAnswerIdx);
}