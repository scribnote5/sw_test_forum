package com.suresoft.sw_test_forum.metric.metric.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.metric.metric.domain.QMetricComment.metricComment;

@Repository
@Transactional
public class MetricCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param metricIdx
     * @return
     */
    public long countAllByMetricIdx(long metricIdx) {
        return queryFactory
                .selectFrom(metricComment)
                .where(metricComment.metricIdx.eq(metricIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param metricIdx
     * @return
     */
    public long deleteAllByMetricIdx(long metricIdx) {
        return queryFactory
                .delete(metricComment)
                .where(metricComment.metricIdx.eq(metricIdx))
                .execute();
    }
}