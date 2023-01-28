package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuidelineAttachedFile.metricGuidelineAttachedFile;

@Repository
@Transactional
public class MetricGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param metricGuidelineIdx
     * @return
     */
    public List<MetricGuidelineAttachedFile> findAttachedFileByMetricGuidelineIdx(long metricGuidelineIdx) {
        return queryFactory
                .selectFrom(metricGuidelineAttachedFile)
                .where(metricGuidelineAttachedFile.metricGuidelineIdx.eq(metricGuidelineIdx))
                .orderBy(metricGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param metricGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByMetricGuidelineIdx(long metricGuidelineIdx) {
        return queryFactory
                .delete(metricGuidelineAttachedFile)
                .where(metricGuidelineAttachedFile.metricGuidelineIdx.eq(metricGuidelineIdx))
                .execute();
    }
}