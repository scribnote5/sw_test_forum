package com.suresoft.sw_test_forum.question_answer.repository;

import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerAttachedFileRepository extends JpaRepository<QuestionAnswerAttachedFile, Long> {

}