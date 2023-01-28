package com.suresoft.sw_test_forum.cwe.cwe_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.cwe.cwe_example.domain.QCweExample.cweExample;

@Repository
@Transactional
public class CweExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweExample> findAllByHighPriorityAsc(long cweIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweExample.class,
                                cweExample.idx,
                                cweExample.createdDate,
                                cweExample.createdByIdx,
                                cweExample.activeStatus,
                                cweExample.views,

                                cweExample.title,
                                cweExample.priority
                        )
                )
                .from(cweExample)
                .where(cweExample.priority.loe(3)
                        .and(cweExample.cweIdx.eq(cweIdx)))
                .orderBy(cweExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public CweExample findAllPriorityByIdx(long idx, long cweIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweExample.class,
                                cweExample.priority
                        )
                )
                .from(cweExample)
                .where(cweExample.idx.eq(idx)
                        .and(cweExample.cweIdx.eq(cweIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweExampleSearchDto
     * @return
     */
    public PageImpl<CweExampleDto> findAll(Pageable pageable, CweExampleSearchDto cweExampleSearchDto) {
        JPQLQuery<CweExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweExampleDto.class,
                                cweExample.idx,
                                cweExample.createdDate,
                                cweExample.createdByIdx,
                                cweExample.activeStatus,
                                cweExample.views,

                                cweExample.cweIdx,
                                cweExample.title,
                                cweExample.priority
                        )
                )
                .from(cweExample)
                .where(searchCondition(cweExampleSearchDto),
                        searchCondition(cweExampleSearchDto.getCweIdx()))
                .orderBy(cweExample.priority.asc(), cweExample.idx.desc());

        long totalCount = query.fetchCount();
        List<CweExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweExampleSearchDto cweExampleSearchDto) {
        BooleanExpression result;

        switch (cweExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = cweExample.title.contains(cweExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cweExample.content.contains(cweExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cweExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweIdx
     * @return
     */
    public List<CweExampleDto> findAll(long cweIdx) {
        JPQLQuery<CweExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweExampleDto.class,
                                cweExample.idx,
                                cweExample.createdDate,
                                cweExample.createdByIdx,
                                cweExample.activeStatus,
                                cweExample.views,

                                cweExample.cweIdx,
                                cweExample.title,
                                cweExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweExample.content,
                                cweExample.nonCompliantExample,
                                cweExample.compliantExample,
                                cweExample.badCasePositionList,
                                cweExample.goodCasePositionList
                        )
                )
                .from(cweExample)
                .join(toolInformation).on(cweExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweExample.cweIdx.eq(cweIdx)
                        .and(cweExample.priority.loe(3)))
                .orderBy(cweExample.priority.asc());

        return query.fetch();
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param cweIdx
     * @return
     */
    private BooleanExpression searchCondition(long cweIdx) {
        BooleanExpression result;

        if (cweIdx == 0) {
            result = null;
        } else {
            result = cweExample.cweIdx.eq(cweIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweExampleDto.class,
                                cweExample.idx,
                                cweExample.createdDate,
                                cweExample.createdByIdx,
                                cweExample.lastModifiedDate,
                                cweExample.lastModifiedByIdx,
                                cweExample.activeStatus,
                                cweExample.views,

                                cweExample.cweIdx,
                                cweExample.title,
                                cweExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                cweExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweExample.compilerInformationIdx,
                                cweExample.content,
                                cweExample.nonCompliantExample,
                                cweExample.compliantExample,
                                cweExample.badCasePositionList,
                                cweExample.goodCasePositionList
                        )
                )
                .from(cweExample)
                .join(toolInformation).on(cweExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweExample.idx.eq(idx))
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
         *  distinct cwe.title
         *
         */
        return queryFactory.select(
                        cweExample.title
                )
                .distinct().from(cweExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cweExample)
                .set(cweExample.views, cweExample.views.add(1))
                .where(cweExample.idx.eq(idx))
                .execute();
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweIdx
     * @return
     */
    public List<CweExampleDto> findAllWhenDelete(long cweIdx) {
        JPQLQuery<CweExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweExampleDto.class,
                                cweExample.idx
                        )
                )
                .from(cweExample)
                .where(cweExample.cweIdx.eq(cweIdx));

        return query.fetch();
    }
}