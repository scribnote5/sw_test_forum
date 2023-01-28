package com.suresoft.sw_test_forum.style_cop.style_cop_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExample;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.QStyleCopExample.styleCopExample;

@Repository
@Transactional
public class StyleCopExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StyleCopExample> findAllByHighPriorityAsc(long styleCopIdx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopExample.class,
                                styleCopExample.idx,
                                styleCopExample.createdDate,
                                styleCopExample.createdByIdx,
                                styleCopExample.activeStatus,
                                styleCopExample.views,

                                styleCopExample.title,
                                styleCopExample.priority
                        )
                )
                .from(styleCopExample)
                .where(styleCopExample.priority.loe(3)
                        .and(styleCopExample.styleCopIdx.eq(styleCopIdx)))
                .orderBy(styleCopExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public StyleCopExample findAllPriorityByIdx(long idx, long styleCopIdx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopExample.class,
                                styleCopExample.priority
                        )
                )
                .from(styleCopExample)
                .where(styleCopExample.idx.eq(idx)
                        .and(styleCopExample.styleCopIdx.eq(styleCopIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param styleCopExampleSearchDto
     * @return
     */
    public PageImpl<StyleCopExampleDto> findAll(Pageable pageable, StyleCopExampleSearchDto styleCopExampleSearchDto) {
        JPQLQuery<StyleCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopExampleDto.class,
                                styleCopExample.idx,
                                styleCopExample.createdDate,
                                styleCopExample.createdByIdx,
                                styleCopExample.activeStatus,
                                styleCopExample.views,

                                styleCopExample.styleCopIdx,
                                styleCopExample.title,
                                styleCopExample.priority
                        )
                )
                .from(styleCopExample)
                .where(searchCondition(styleCopExampleSearchDto),
                        searchCondition(styleCopExampleSearchDto.getStyleCopIdx()))
                .orderBy(styleCopExample.priority.asc(), styleCopExample.idx.desc());

        long totalCount = query.fetchCount();
        List<StyleCopExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param styleCopExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(StyleCopExampleSearchDto styleCopExampleSearchDto) {
        BooleanExpression result;

        switch (styleCopExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = styleCopExample.title.contains(styleCopExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = styleCopExample.content.contains(styleCopExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = styleCopExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(styleCopExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * StyleCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    public List<StyleCopExampleDto> findAll(long styleCopIdx) {
        JPQLQuery<StyleCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopExampleDto.class,
                                styleCopExample.idx,
                                styleCopExample.createdDate,
                                styleCopExample.createdByIdx,
                                styleCopExample.activeStatus,
                                styleCopExample.views,

                                styleCopExample.styleCopIdx,
                                styleCopExample.title,
                                styleCopExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                styleCopExample.content,
                                styleCopExample.nonCompliantExample,
                                styleCopExample.compliantExample,
                                styleCopExample.badCasePositionList,
                                styleCopExample.goodCasePositionList
                        )
                )
                .from(styleCopExample)
                .join(toolInformation).on(styleCopExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(styleCopExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(styleCopExample.styleCopIdx.eq(styleCopIdx)
                        .and(styleCopExample.priority.loe(3)))
                .orderBy(styleCopExample.priority.asc());

        return query.fetch();
    }

    /**
     * StyleCop 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param styleCopIdx
     * @return
     */
    private BooleanExpression searchCondition(long styleCopIdx) {
        BooleanExpression result;

        if (styleCopIdx == 0) {
            result = null;
        } else {
            result = styleCopExample.styleCopIdx.eq(styleCopIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StyleCopExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopExampleDto.class,
                                styleCopExample.idx,
                                styleCopExample.createdDate,
                                styleCopExample.createdByIdx,
                                styleCopExample.lastModifiedDate,
                                styleCopExample.lastModifiedByIdx,
                                styleCopExample.activeStatus,
                                styleCopExample.views,

                                styleCopExample.styleCopIdx,
                                styleCopExample.title,
                                styleCopExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                styleCopExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                styleCopExample.compilerInformationIdx,
                                styleCopExample.content,
                                styleCopExample.nonCompliantExample,
                                styleCopExample.compliantExample,
                                styleCopExample.badCasePositionList,
                                styleCopExample.goodCasePositionList
                        )
                )
                .from(styleCopExample)
                .join(toolInformation).on(styleCopExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(styleCopExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(styleCopExample.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        /*
         * SELECT
         *  distinct styleCop.title
         *
         */
        return queryFactory.select(
                        styleCopExample.title
                )
                .distinct().from(styleCopExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(styleCopExample)
                .set(styleCopExample.views, styleCopExample.views.add(1))
                .where(styleCopExample.idx.eq(idx))
                .execute();
    }

    /**
     * StyleCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    public List<StyleCopExampleDto> findAllWhenDelete(long styleCopIdx) {
        JPQLQuery<StyleCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopExampleDto.class,
                                styleCopExample.idx
                        )
                )
                .from(styleCopExample)
                .where(styleCopExample.styleCopIdx.eq(styleCopIdx));

        return query.fetch();
    }
}