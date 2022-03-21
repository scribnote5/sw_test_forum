package com.suresoft.sw_test_forum.metric.metric.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.metric.metric.domain.QMetricAttachedFile.metricAttachedFile;

@Repository
@Transactional
public class MetricAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    public List<MetricAttachedFile> findAttachedFileByMetricIdx(long metricIdx) {
        return queryFactory
                .selectFrom(metricAttachedFile)
                .where(metricAttachedFile.metricIdx.eq(metricIdx))
                .orderBy(metricAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param metricIdx
     * @return
     */
    public long deleteAttachedFileByMetricIdx(long metricIdx) {
        return queryFactory
                .delete(metricAttachedFile)
                .where(metricAttachedFile.metricIdx.eq(metricIdx))
                .execute();
    }
}