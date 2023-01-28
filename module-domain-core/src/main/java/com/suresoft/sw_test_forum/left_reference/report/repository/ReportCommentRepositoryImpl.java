package com.suresoft.sw_test_forum.left_reference.report.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.left_reference.report.domain.QReportComment.reportComment;

@Repository
@Transactional
public class ReportCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ReportCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ReportComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param reportIdx
     * @return
     */
    public long countAllByReportIdx(long reportIdx) {
        return queryFactory
                .selectFrom(reportComment)
                .where(reportComment.reportIdx.eq(reportIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param reportIdx
     * @return
     */
    public long deleteAllByReportIdx(long reportIdx) {
        return queryFactory
                .delete(reportComment)
                .where(reportComment.reportIdx.eq(reportIdx))
                .execute();
    }
}