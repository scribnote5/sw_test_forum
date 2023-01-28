package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.user.domain.QUser;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExample;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.QMisraCppExample;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.QMisraCppExample.misraCppExample;

@Repository
@Transactional
public class MisraCppExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCppExample> findAllByHighPriorityAsc(long misraCppIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppExample.class,
                                misraCppExample.idx,
                                misraCppExample.createdDate,
                                misraCppExample.createdByIdx,
                                misraCppExample.activeStatus,
                                misraCppExample.views,

                                misraCppExample.title,
                                misraCppExample.priority
                        )
                )
                .from(misraCppExample)
                .where(misraCppExample.priority.loe(3)
                        .and(misraCppExample.misraCppIdx.eq(misraCppIdx)))
                .orderBy(misraCppExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public MisraCppExample findAllPriorityByIdx(long idx, long misraCppIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppExample.class,
                                misraCppExample.priority
                        )
                )
                .from(misraCppExample)
                .where(misraCppExample.idx.eq(idx)
                        .and(misraCppExample.misraCppIdx.eq(misraCppIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCppExampleSearchDto
     * @return
     */
    public PageImpl<MisraCppExampleDto> findAll(Pageable pageable, MisraCppExampleSearchDto misraCppExampleSearchDto) {
        JPQLQuery<MisraCppExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppExampleDto.class,
                                misraCppExample.idx,
                                misraCppExample.createdDate,
                                misraCppExample.createdByIdx,
                                misraCppExample.activeStatus,
                                misraCppExample.views,

                                misraCppExample.misraCppIdx,
                                misraCppExample.title,
                                misraCppExample.priority
                        )
                )
                .from(misraCppExample)
                .where(searchCondition(misraCppExampleSearchDto),
                        searchCondition(misraCppExampleSearchDto.getMisraCppIdx()))
                .orderBy(misraCppExample.priority.asc(), misraCppExample.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCppExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCppExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCppExampleSearchDto misraCppExampleSearchDto) {
        BooleanExpression result;

        switch (misraCppExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = misraCppExample.title.contains(misraCppExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraCppExample.content.contains(misraCppExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraCppExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCppExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * MISRA CPP 규칙에서 이동한 예제 코드 리스트 페이지 일 때, 리스트 조회 조건
     *
     * @param misraCppIdx
     * @return
     */
    private BooleanExpression searchCondition(long misraCppIdx) {
        BooleanExpression result;

        if (misraCppIdx == 0) {
            result = null;
        } else {
            result = misraCppExample.misraCppIdx.eq(misraCppIdx);
        }

        return result;
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    public List<MisraCppExampleDto> findAll(long misraCppIdx) {
        JPQLQuery<MisraCppExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppExampleDto.class,
                                misraCppExample.idx,
                                misraCppExample.createdDate,
                                misraCppExample.createdByIdx,
                                misraCppExample.activeStatus,
                                misraCppExample.views,

                                misraCppExample.misraCppIdx,
                                misraCppExample.title,
                                misraCppExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCppExample.content,
                                misraCppExample.nonCompliantExample,
                                misraCppExample.compliantExample,
                                misraCppExample.badCasePositionList,
                                misraCppExample.goodCasePositionList
                        )
                )
                .from(misraCppExample)
                .join(toolInformation).on(misraCppExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCppExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCppExample.misraCppIdx.eq(misraCppIdx)
                        .and(misraCppExample.priority.loe(3)))
                .orderBy(misraCppExample.priority.asc());

        return query.fetch();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCppExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppExampleDto.class,
                                misraCppExample.idx,
                                misraCppExample.createdDate,
                                misraCppExample.createdByIdx,
                                misraCppExample.lastModifiedDate,
                                misraCppExample.lastModifiedByIdx,
                                misraCppExample.activeStatus,
                                misraCppExample.views,

                                misraCppExample.misraCppIdx,
                                misraCppExample.title,
                                misraCppExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                misraCppExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCppExample.compilerInformationIdx,
                                misraCppExample.content,
                                misraCppExample.nonCompliantExample,
                                misraCppExample.compliantExample,
                                misraCppExample.badCasePositionList,
                                misraCppExample.goodCasePositionList
                        )
                )
                .from(misraCppExample)
                .join(toolInformation).on(misraCppExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCppExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCppExample.idx.eq(idx))
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
         *  distinct misraCpp.title
         *
         */
        return queryFactory.select(
                        misraCppExample.title
                )
                .distinct().from(misraCppExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraCppExample)
                .set(misraCppExample.views, misraCppExample.views.add(1))
                .where(misraCppExample.idx.eq(idx))
                .execute();
    }


    /**
     * MISRA CPP 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    public List<MisraCppExampleDto> findAllWhenDelete(long misraCppIdx) {
        JPQLQuery<MisraCppExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppExampleDto.class,
                                misraCppExample.idx
                        )
                )
                .from(misraCppExample)
                .where(misraCppExample.misraCppIdx.eq(misraCppIdx));

        return query.fetch();
    }
}