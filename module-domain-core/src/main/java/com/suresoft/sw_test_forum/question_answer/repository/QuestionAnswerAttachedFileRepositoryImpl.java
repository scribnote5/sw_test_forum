package com.suresoft.sw_test_forum.question_answer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.question_answer.domain.QQuestionAnswerAttachedFile.questionAnswerAttachedFile;

@Repository
@Transactional
public class QuestionAnswerAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public QuestionAnswerAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(QuestionAnswerAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param questionAnswerIdx
     * @return
     */
    public List<QuestionAnswerAttachedFile> findAttachedFileByQuestionAnswerIdx(long questionAnswerIdx) {
        return queryFactory
                .selectFrom(questionAnswerAttachedFile)
                .where(questionAnswerAttachedFile.questionAnswerIdx.eq(questionAnswerIdx))
                .orderBy(questionAnswerAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param questionAnswerIdx
     * @return
     */
    public long deleteAttachedFileByQuestionAnswerIdx(long questionAnswerIdx) {
        return queryFactory
                .delete(questionAnswerAttachedFile)
                .where(questionAnswerAttachedFile.questionAnswerIdx.eq(questionAnswerIdx))
                .execute();
    }
}