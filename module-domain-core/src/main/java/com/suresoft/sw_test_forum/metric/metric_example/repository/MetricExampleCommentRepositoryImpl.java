package com.suresoft.sw_test_forum.metric.metric_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.metric.metric_example.domain.QMetricExampleComment.metricExampleComment;

@Repository
@Transactional
public class MetricExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param metricExampleIdx
     * @return
     */
    public long countAllByMetricExampleIdx(long metricExampleIdx) {
        return queryFactory
                .selectFrom(metricExampleComment)
                .where(metricExampleComment.metricExampleIdx.eq(metricExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param metricExampleIdx
     * @return
     */
    public long deleteAllByMetricExampleIdx(long metricExampleIdx) {
        return queryFactory
                .delete(metricExampleComment)
                .where(metricExampleComment.metricExampleIdx.eq(metricExampleIdx))
                .execute();
    }
}