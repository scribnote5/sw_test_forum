package com.suresoft.sw_test_forum.question_answer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswer;
import com.suresoft.sw_test_forum.question_answer.domain.enums.QuestionAnswerType;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.question_answer.domain.QQuestionAnswer.questionAnswer;

@Repository
@Transactional
public class QuestionAnswerRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public QuestionAnswerRepositoryImpl(JPAQueryFactory queryFactory) {
        super(QuestionAnswer.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param questionAnswerSearchDto
     * @return
     */
    public PageImpl<QuestionAnswerDto> findAll(Pageable pageable, QuestionAnswerSearchDto questionAnswerSearchDto) {
        JPQLQuery<QuestionAnswerDto> query = queryFactory.select(
                        Projections.bean(
                                QuestionAnswerDto.class,
                                questionAnswer.idx,
                                questionAnswer.createdDate,
                                questionAnswer.createdByIdx,
                                questionAnswer.activeStatus,
                                questionAnswer.views,

                                questionAnswer.title,
                                questionAnswer.type
                        )
                )
                .from(questionAnswer)
                .where(searchCondition(questionAnswerSearchDto))
                .orderBy(questionAnswer.groupIdx.desc(), questionAnswer.idx.asc());

        long totalCount = query.fetchCount();
        List<QuestionAnswerDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param questionAnswerSearchDto
     * @return
     */
    private BooleanExpression searchCondition(QuestionAnswerSearchDto questionAnswerSearchDto) {
        BooleanExpression result;

        switch (questionAnswerSearchDto.getSearchType()) {
            case "TITLE":
                result = questionAnswer.title.contains(questionAnswerSearchDto.getSearchKeyword());
                break;
            case "TYPE":
                result = questionAnswer.type.eq(QuestionAnswerType.valueOf(questionAnswerSearchDto.getSearchKeyword()));
                break;
            case "CONTENT":
                result = questionAnswer.content.contains(questionAnswerSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = questionAnswer.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(questionAnswerSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<QuestionAnswerDto> findTop10() {
        JPQLQuery<QuestionAnswerDto> query = queryFactory.select(
                        Projections.bean(
                                QuestionAnswerDto.class,
                                questionAnswer.idx,
                                questionAnswer.createdDate,
                                questionAnswer.createdByIdx,
                                questionAnswer.activeStatus,
                                questionAnswer.views,

                                questionAnswer.title
                        )
                )
                .from(questionAnswer)
                .orderBy(questionAnswer.groupIdx.desc(), questionAnswer.idx.asc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 현재 게시글 이외 질문과 연관된 게시글 리스트 조회
     *
     * @param idx
     * @param groupIdx
     * @return
     */
    public List<QuestionAnswerDto> findAllByIdxAndGroupIdx(long idx, long groupIdx) {
        JPQLQuery<QuestionAnswerDto> query = queryFactory.select(
                        Projections.bean(
                                QuestionAnswerDto.class,
                                questionAnswer.idx,
                                questionAnswer.createdDate,
                                questionAnswer.createdByIdx,
                                questionAnswer.activeStatus,
                                questionAnswer.views,

                                questionAnswer.title,
                                questionAnswer.type,
                                questionAnswer.content
                        )
                )
                .from(questionAnswer)
                .where(questionAnswer.idx.lt(idx).and(questionAnswer.groupIdx.eq(groupIdx)))
                .orderBy(questionAnswer.groupIdx.desc(), questionAnswer.idx.asc());

        return query.fetch();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        questionAnswer.title
                )
                .distinct().from(questionAnswer)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory
                .update(questionAnswer)
                .set(questionAnswer.views, questionAnswer.views.add(1))
                .where(questionAnswer.idx.eq(idx))
                .execute();
    }
}