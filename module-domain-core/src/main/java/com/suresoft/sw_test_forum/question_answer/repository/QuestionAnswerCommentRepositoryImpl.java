package com.suresoft.sw_test_forum.question_answer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.question_answer.domain.QQuestionAnswerComment.questionAnswerComment;

@Repository
@Transactional
public class QuestionAnswerCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public QuestionAnswerCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(QuestionAnswerComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param questionAnswerIdx
     * @return
     */
    public long countAllByQuestionAnswerIdx(long questionAnswerIdx) {
        return queryFactory
                .selectFrom(questionAnswerComment)
                .where(questionAnswerComment.questionAnswerIdx.eq(questionAnswerIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param questionAnswerIdx
     * @return
     */
    public long deleteAllByQuestionAnswerIdx(long questionAnswerIdx) {
        return queryFactory
                .delete(questionAnswerComment)
                .where(questionAnswerComment.questionAnswerIdx.eq(questionAnswerIdx))
                .execute();
    }
}