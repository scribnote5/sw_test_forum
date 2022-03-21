package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuidelineComment.metricGuidelineComment;

@Repository
@Transactional
public class MetricGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param metricGuidelineIdx
     * @return
     */
    public long countAllByMetricGuidelineCIdx(long metricGuidelineIdx) {
        return queryFactory
                .selectFrom(metricGuidelineComment)
                .where(metricGuidelineComment.metricGuidelineIdx.eq(metricGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param metricGuidelineIdx
     * @return
     */
    public long deleteAllByMetricGuidelineIdx(long metricGuidelineIdx) {
        return queryFactory
                .delete(metricGuidelineComment)
                .where(metricGuidelineComment.metricGuidelineIdx.eq(metricGuidelineIdx))
                .execute();
    }
}