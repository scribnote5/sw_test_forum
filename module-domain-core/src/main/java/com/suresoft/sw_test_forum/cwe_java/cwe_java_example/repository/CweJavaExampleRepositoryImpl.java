package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExample;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.QCweJavaExample.cweJavaExample;

@Repository
@Transactional
public class CweJavaExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweJavaExample> findAllByHighPriorityAsc(long cweJavaIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaExample.class,
                                cweJavaExample.idx,
                                cweJavaExample.createdDate,
                                cweJavaExample.createdByIdx,
                                cweJavaExample.activeStatus,
                                cweJavaExample.views,

                                cweJavaExample.title,
                                cweJavaExample.priority
                        )
                )
                .from(cweJavaExample)
                .where(cweJavaExample.priority.loe(3)
                        .and(cweJavaExample.cweJavaIdx.eq(cweJavaIdx)))
                .orderBy(cweJavaExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public CweJavaExample findAllPriorityByIdx(long idx, long cweJavaIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaExample.class,
                                cweJavaExample.priority
                        )
                )
                .from(cweJavaExample)
                .where(cweJavaExample.idx.eq(idx)
                        .and(cweJavaExample.cweJavaIdx.eq(cweJavaIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweJavaExampleSearchDto
     * @return
     */
    public PageImpl<CweJavaExampleDto> findAll(Pageable pageable, CweJavaExampleSearchDto cweJavaExampleSearchDto) {
        JPQLQuery<CweJavaExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaExampleDto.class,
                                cweJavaExample.idx,
                                cweJavaExample.createdDate,
                                cweJavaExample.createdByIdx,
                                cweJavaExample.activeStatus,
                                cweJavaExample.views,

                                cweJavaExample.cweJavaIdx,
                                cweJavaExample.title,
                                cweJavaExample.priority
                        )
                )
                .from(cweJavaExample)
                .where(searchCondition(cweJavaExampleSearchDto),
                        searchCondition(cweJavaExampleSearchDto.getCweJavaIdx()))
                .orderBy(cweJavaExample.priority.asc(), cweJavaExample.idx.desc());

        long totalCount = query.fetchCount();
        List<CweJavaExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweJavaExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweJavaExampleSearchDto cweJavaExampleSearchDto) {
        BooleanExpression result;

        switch (cweJavaExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = cweJavaExample.title.contains(cweJavaExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cweJavaExample.content.contains(cweJavaExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cweJavaExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweJavaExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public List<CweJavaExampleDto> findAll(long cweJavaIdx) {
        JPQLQuery<CweJavaExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaExampleDto.class,
                                cweJavaExample.idx,
                                cweJavaExample.createdDate,
                                cweJavaExample.createdByIdx,
                                cweJavaExample.activeStatus,
                                cweJavaExample.views,

                                cweJavaExample.cweJavaIdx,
                                cweJavaExample.title,
                                cweJavaExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweJavaExample.content,
                                cweJavaExample.nonCompliantExample,
                                cweJavaExample.compliantExample,
                                cweJavaExample.badCasePositionList,
                                cweJavaExample.goodCasePositionList
                        )
                )
                .from(cweJavaExample)
                .join(toolInformation).on(cweJavaExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweJavaExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweJavaExample.cweJavaIdx.eq(cweJavaIdx)
                        .and(cweJavaExample.priority.loe(3)))
                .orderBy(cweJavaExample.priority.asc());

        return query.fetch();
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param cweJavaIdx
     * @return
     */
    private BooleanExpression searchCondition(long cweJavaIdx) {
        BooleanExpression result;

        if (cweJavaIdx == 0) {
            result = null;
        } else {
            result = cweJavaExample.cweJavaIdx.eq(cweJavaIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaExampleDto.class,
                                cweJavaExample.idx,
                                cweJavaExample.createdDate,
                                cweJavaExample.createdByIdx,
                                cweJavaExample.lastModifiedDate,
                                cweJavaExample.lastModifiedByIdx,
                                cweJavaExample.activeStatus,
                                cweJavaExample.views,

                                cweJavaExample.cweJavaIdx,
                                cweJavaExample.title,
                                cweJavaExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                cweJavaExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweJavaExample.compilerInformationIdx,
                                cweJavaExample.content,
                                cweJavaExample.nonCompliantExample,
                                cweJavaExample.compliantExample,
                                cweJavaExample.badCasePositionList,
                                cweJavaExample.goodCasePositionList
                        )
                )
                .from(cweJavaExample)
                .join(toolInformation).on(cweJavaExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweJavaExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweJavaExample.idx.eq(idx))
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
                        cweJavaExample.title
                )
                .distinct().from(cweJavaExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cweJavaExample)
                .set(cweJavaExample.views, cweJavaExample.views.add(1))
                .where(cweJavaExample.idx.eq(idx))
                .execute();
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public List<CweJavaExampleDto> findAllWhenDelete(long cweJavaIdx) {
        JPQLQuery<CweJavaExampleDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaExampleDto.class,
                                cweJavaExample.idx
                        )
                )
                .from(cweJavaExample)
                .where(cweJavaExample.cweJavaIdx.eq(cweJavaIdx));

        return query.fetch();
    }
}