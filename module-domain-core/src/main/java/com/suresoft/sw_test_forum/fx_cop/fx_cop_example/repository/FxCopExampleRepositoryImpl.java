package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExample;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.QFxCopExample.fxCopExample;

@Repository
@Transactional
public class FxCopExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<FxCopExample> findAllByHighPriorityAsc(long fxCopIdx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopExample.class,
                                fxCopExample.idx,
                                fxCopExample.createdDate,
                                fxCopExample.createdByIdx,
                                fxCopExample.activeStatus,
                                fxCopExample.views,

                                fxCopExample.title,
                                fxCopExample.priority
                        )
                )
                .from(fxCopExample)
                .where(fxCopExample.priority.loe(3)
                        .and(fxCopExample.fxCopIdx.eq(fxCopIdx)))
                .orderBy(fxCopExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public FxCopExample findAllPriorityByIdx(long idx, long fxCopIdx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopExample.class,
                                fxCopExample.priority
                        )
                )
                .from(fxCopExample)
                .where(fxCopExample.idx.eq(idx)
                        .and(fxCopExample.fxCopIdx.eq(fxCopIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param fxCopExampleSearchDto
     * @return
     */
    public PageImpl<FxCopExampleDto> findAll(Pageable pageable, FxCopExampleSearchDto fxCopExampleSearchDto) {
        JPQLQuery<FxCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopExampleDto.class,
                                fxCopExample.idx,
                                fxCopExample.createdDate,
                                fxCopExample.createdByIdx,
                                fxCopExample.activeStatus,
                                fxCopExample.views,

                                fxCopExample.fxCopIdx,
                                fxCopExample.title,
                                fxCopExample.priority
                        )
                )
                .from(fxCopExample)
                .where(searchCondition(fxCopExampleSearchDto),
                        searchCondition(fxCopExampleSearchDto.getFxCopIdx()))
                .orderBy(fxCopExample.priority.asc(), fxCopExample.idx.desc());

        long totalCount = query.fetchCount();
        List<FxCopExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param fxCopExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(FxCopExampleSearchDto fxCopExampleSearchDto) {
        BooleanExpression result;

        switch (fxCopExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = fxCopExample.title.contains(fxCopExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = fxCopExample.content.contains(fxCopExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = fxCopExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(fxCopExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * FxCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    public List<FxCopExampleDto> findAll(long fxCopIdx) {
        JPQLQuery<FxCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopExampleDto.class,
                                fxCopExample.idx,
                                fxCopExample.createdDate,
                                fxCopExample.createdByIdx,
                                fxCopExample.activeStatus,
                                fxCopExample.views,

                                fxCopExample.fxCopIdx,
                                fxCopExample.title,
                                fxCopExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                fxCopExample.content,
                                fxCopExample.nonCompliantExample,
                                fxCopExample.compliantExample,
                                fxCopExample.badCasePositionList,
                                fxCopExample.goodCasePositionList
                        )
                )
                .from(fxCopExample)
                .join(toolInformation).on(fxCopExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(fxCopExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(fxCopExample.fxCopIdx.eq(fxCopIdx)
                        .and(fxCopExample.priority.loe(3)))
                .orderBy(fxCopExample.priority.asc());

        return query.fetch();
    }

    /**
     * FxCop 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param fxCopIdx
     * @return
     */
    private BooleanExpression searchCondition(long fxCopIdx) {
        BooleanExpression result;

        if (fxCopIdx == 0) {
            result = null;
        } else {
            result = fxCopExample.fxCopIdx.eq(fxCopIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public FxCopExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopExampleDto.class,
                                fxCopExample.idx,
                                fxCopExample.createdDate,
                                fxCopExample.createdByIdx,
                                fxCopExample.lastModifiedDate,
                                fxCopExample.lastModifiedByIdx,
                                fxCopExample.activeStatus,
                                fxCopExample.views,

                                fxCopExample.fxCopIdx,
                                fxCopExample.title,
                                fxCopExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                fxCopExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                fxCopExample.compilerInformationIdx,
                                fxCopExample.content,
                                fxCopExample.nonCompliantExample,
                                fxCopExample.compliantExample,
                                fxCopExample.badCasePositionList,
                                fxCopExample.goodCasePositionList
                        )
                )
                .from(fxCopExample)
                .join(toolInformation).on(fxCopExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(fxCopExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(fxCopExample.idx.eq(idx))
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
         *  distinct fxCop.title
         *
         */
        return queryFactory.select(
                        fxCopExample.title
                )
                .distinct().from(fxCopExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(fxCopExample)
                .set(fxCopExample.views, fxCopExample.views.add(1))
                .where(fxCopExample.idx.eq(idx))
                .execute();
    }

    /**
     * FxCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    public List<FxCopExampleDto> findAllWhenDelete(long fxCopIdx) {
        JPQLQuery<FxCopExampleDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopExampleDto.class,
                                fxCopExample.idx
                        )
                )
                .from(fxCopExample)
                .where(fxCopExample.fxCopIdx.eq(fxCopIdx));

        return query.fetch();
    }
}