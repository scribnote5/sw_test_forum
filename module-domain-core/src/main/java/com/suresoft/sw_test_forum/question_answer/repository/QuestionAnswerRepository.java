package com.suresoft.sw_test_forum.question_answer.repository;

import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

}