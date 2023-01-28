package com.suresoft.sw_test_forum.metric.metric_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExample;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.metric.metric_example.domain.QMetricExample.metricExample;
import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuideline.metricGuideline;

@Repository
@Transactional
public class MetricExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MetricExample> findAllByHighPriorityAsc(long metricIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricExample.class,
                                metricExample.idx,
                                metricExample.createdDate,
                                metricExample.createdByIdx,
                                metricExample.activeStatus,
                                metricExample.views,

                                metricExample.title,
                                metricExample.priority
                        )
                )
                .from(metricExample)
                .where(metricExample.priority.loe(3)
                        .and(metricExample.metricIdx.eq(metricIdx)))
                .orderBy(metricExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public MetricExample findAllPriorityByIdx(long idx, long metricIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricExample.class,
                                metricExample.priority
                        )
                )
                .from(metricExample)
                .where(metricExample.idx.eq(idx)
                        .and(metricExample.metricIdx.eq(metricIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param metricExampleSearchDto
     * @return
     */
    public PageImpl<MetricExampleDto> findAll(Pageable pageable, MetricExampleSearchDto metricExampleSearchDto) {
        JPQLQuery<MetricExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MetricExampleDto.class,
                                metricExample.idx,
                                metricExample.createdDate,
                                metricExample.createdByIdx,
                                metricExample.activeStatus,
                                metricExample.views,

                                metricExample.metricIdx,
                                metricExample.title,
                                metricExample.priority
                        )
                )
                .from(metricExample)
                .where(searchCondition(metricExampleSearchDto),
                        searchCondition(metricExampleSearchDto.getMetricIdx()))
                .orderBy(metricExample.priority.asc(), metricExample.idx.desc());

        long totalCount = query.fetchCount();
        List<MetricExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param metricExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MetricExampleSearchDto metricExampleSearchDto) {
        BooleanExpression result;

        switch (metricExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = metricExample.title.contains(metricExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = metricExample.content.contains(metricExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = metricExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(metricExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    public List<MetricExampleDto> findAll(long metricIdx) {
        JPQLQuery<MetricExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MetricExampleDto.class,
                                metricExample.idx,
                                metricExample.createdDate,
                                metricExample.createdByIdx,
                                metricExample.activeStatus,
                                metricExample.views,

                                metricExample.metricIdx,
                                metricExample.title,
                                metricExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                metricExample.content,
                                metricExample.nonCompliantExample,
                                metricExample.compliantExample,
                                metricExample.badCasePositionList,
                                metricExample.goodCasePositionList
                        )
                )
                .from(metricExample)
                .join(toolInformation).on(metricExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(metricExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(metricExample.metricIdx.eq(metricIdx)
                        .and(metricExample.priority.loe(3)))
                .orderBy(metricExample.priority.asc());

        return query.fetch();
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param metricIdx
     * @return
     */
    private BooleanExpression searchCondition(long metricIdx) {
        BooleanExpression result;

        if (metricIdx == 0) {
            result = null;
        } else {
            result = metricExample.metricIdx.eq(metricIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricExampleDto.class,
                                metricExample.idx,
                                metricExample.createdDate,
                                metricExample.createdByIdx,
                                metricExample.lastModifiedDate,
                                metricExample.lastModifiedByIdx,
                                metricExample.activeStatus,
                                metricExample.views,

                                metricExample.metricIdx,
                                metricExample.title,
                                metricExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                metricExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                metricExample.compilerInformationIdx,
                                metricExample.content,
                                metricExample.nonCompliantExample,
                                metricExample.compliantExample,
                                metricExample.badCasePositionList,
                                metricExample.goodCasePositionList
                        )
                )
                .from(metricExample)
                .join(toolInformation).on(metricExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(metricExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(metricExample.idx.eq(idx))
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
         *  distinct metric.title
         *
         */
        return queryFactory.select(
                        metricExample.title
                )
                .distinct().from(metricExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(metricExample)
                .set(metricExample.views, metricExample.views.add(1))
                .where(metricExample.idx.eq(idx))
                .execute();
    }

    /**
     * 메트릭 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    public List<MetricExampleDto> findAllWhenDelete(long metricIdx) {
        JPQLQuery<MetricExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MetricExampleDto.class,
                                metricExample.idx
                        )
                )
                .from(metricExample)
                .where(metricGuideline.metricIdx.eq(metricIdx));

        return query.fetch();
    }
}