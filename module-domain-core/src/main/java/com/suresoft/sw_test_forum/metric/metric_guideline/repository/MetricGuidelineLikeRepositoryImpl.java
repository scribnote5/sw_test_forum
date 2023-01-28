package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuidelineLike.metricGuidelineLike;

@Repository
@Transactional
public class MetricGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public MetricGuidelineLikeDto findByUsernameAndMetricGuidelineIdx(String username, long metricGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricGuidelineLikeDto.class,
                                metricGuidelineLike.idx,
                                metricGuidelineLike.createdDate,
                                metricGuidelineLike.createdByIdx,
                                metricGuidelineLike.lastModifiedDate,
                                metricGuidelineLike.lastModifiedByIdx,
                                metricGuidelineLike.activeStatus,
                                metricGuidelineLike.views,

                                metricGuidelineLike.metricGuidelineIdx
                        )
                )
                .from(metricGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(metricGuidelineLike.metricGuidelineIdx.eq(metricGuidelineIdx)
                        .and(metricGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param metricGuidelineLikeIdx
     * @return
     */
    public long deleteAllByMetricGuidelineIdx(long metricGuidelineLikeIdx) {
        return queryFactory
                .delete(metricGuidelineLike)
                .where(metricGuidelineLike.metricGuidelineIdx.eq(metricGuidelineLikeIdx))
                .execute();
    }
}