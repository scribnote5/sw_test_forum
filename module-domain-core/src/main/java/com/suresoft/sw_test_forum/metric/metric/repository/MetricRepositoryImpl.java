package com.suresoft.sw_test_forum.metric.metric.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric.domain.Metric;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.metric.metric.domain.QMetric.metric;

@Repository
@Transactional
public class MetricRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Metric.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MetricDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                MetricDto.class,
                                metric.idx,
                                metric.createdDate,
                                metric.createdByIdx,
                                metric.activeStatus,
                                metric.views,

                                metric.title,
                                metric.priority,
                                metric.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(metric)
                .join(hashTags).on(metric.hashTagsIdx.eq(hashTags.idx))
                .where(metric.priority.loe(5))
                .orderBy(metric.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<MetricDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                MetricDto.class,
                                metric.priority
                        )
                )
                .from(metric)
                .where(metric.priority.loe(5))
                .orderBy(metric.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param metricSearchDto
     * @return
     */
    public PageImpl<MetricDto> findAll(Pageable pageable, MetricSearchDto metricSearchDto) {
        JPQLQuery<MetricDto> query = queryFactory.select(
                        Projections.bean(
                                MetricDto.class,
                                metric.idx,
                                metric.createdDate,
                                metric.createdByIdx,
                                metric.activeStatus,
                                metric.views,

                                metric.title,
                                metric.priority,
                                metric.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(metric)
                .join(hashTags).on(metric.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(metricSearchDto))
                .orderBy(metric.idx.desc());

        long totalCount = query.fetchCount();
        List<MetricDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param metricSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MetricSearchDto metricSearchDto) {
        BooleanExpression result;

        switch (metricSearchDto.getSearchType()) {
            case "TITLE":
                result = metric.title.contains(metricSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(metricSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = metric.content.contains(metricSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = metric.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(metricSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(metricSearchDto.getSearchKeyword())) {
            result = result.and(metric.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricDto.class,
                                metric.idx,
                                metric.createdDate,
                                metric.createdByIdx,
                                metric.activeStatus,
                                metric.lastModifiedDate,
                                metric.lastModifiedByIdx,
                                metric.views,

                                metric.title,
                                metric.priority,
                                metric.frequency,
                                hashTags.content.as("hashTags"),
                                metric.hashTagsIdx,
                                metric.content
                        )
                )
                .from(metric)
                .join(hashTags).on(metric.hashTagsIdx.eq(hashTags.idx))
                .where(metric.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        metric.title
                )
                .distinct().from(metric)
                .fetch();
    }

    /**
     * 메트릭 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public Metric findMetricByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Metric.class,
                                metric.title,
                                metric.priority
                        )
                )
                .from(metric)
                .where(metric.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public Metric findMetricPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Metric.class,
                                metric.priority
                        )
                )
                .from(metric)
                .where(metric.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(metric)
                .set(metric.views, metric.views.add(1))
                .where(metric.idx.eq(idx))
                .execute();
    }
}