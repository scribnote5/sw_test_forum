package com.suresoft.sw_test_forum.left_reference.report.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.left_reference.report.domain.QReportAttachedFile.reportAttachedFile;

@Repository
@Transactional
public class ReportAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ReportAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ReportAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param reportIdx
     * @return
     */
    public List<ReportAttachedFile> findAttachedFileByReportIdx(long reportIdx) {
        return queryFactory
                .selectFrom(reportAttachedFile)
                .where(reportAttachedFile.reportIdx.eq(reportIdx))
                .orderBy(reportAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param reportIdx
     * @return
     */
    public long deleteAttachedFileByReportIdx(long reportIdx) {
        return queryFactory
                .delete(reportAttachedFile)
                .where(reportAttachedFile.reportIdx.eq(reportIdx))
                .execute();
    }
}