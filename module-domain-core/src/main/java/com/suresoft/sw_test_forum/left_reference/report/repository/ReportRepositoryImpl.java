package com.suresoft.sw_test_forum.left_reference.report.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.report.domain.Report;
import com.suresoft.sw_test_forum.left_reference.report.domain.enums.Category;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.left_reference.report.domain.QReport.report;

@Repository
@Transactional
public class ReportRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ReportRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Report.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ReportDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ReportDto.class,
                                report.idx,
                                report.createdDate,
                                report.createdByIdx,
                                report.activeStatus,
                                report.views,

                                report.title,
                                report.priority,
                                report.category
                        )
                )
                .from(report)
                .where(report.priority.loe(5))
                .orderBy(report.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @param idx
     * @return
     */
    public Report findAllByHighPriorityAscCheckPriority(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Report.class,
                                report.priority
                        )
                )
                .from(report)
                .where(report.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선수위 낮은 리스트 조회
     *
     * @param pageable
     * @param reportSearchDto
     * @return
     */
    public PageImpl<ReportDto> findAll(Pageable pageable, ReportSearchDto reportSearchDto) {
        JPQLQuery<ReportDto> query = queryFactory.select(
                        Projections.bean(
                                ReportDto.class,
                                report.idx,
                                report.createdDate,
                                report.createdByIdx,
                                report.activeStatus,
                                report.views,

                                report.title,
                                report.priority,
                                report.category
                        )
                )
                .from(report)
                .where(searchCondition(reportSearchDto)
                        .and(searchCondition2(reportSearchDto)))
                .orderBy(report.idx.desc());

        long totalCount = query.fetchCount();
        List<ReportDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param reportSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ReportSearchDto reportSearchDto) {
        BooleanExpression result;

        switch (reportSearchDto.getSearchType()) {
            case "TITLE":
                result = report.title.contains(reportSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = report.content.contains(reportSearchDto.getSearchKeyword());
                break;
            case "CATEGORY":
                result = report.category.eq(Category.valueOf(reportSearchDto.getSearchKeyword()));
                break;
            case "CREATED_BY":
                result = report.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(reportSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(reportSearchDto.getSearchKeyword())) {
            result = result.and(report.priority.goe(6));
        }

        return result;
    }

    /**
     * 리스트 조회 조건
     *
     * @param reportSearchDto
     * @return
     */
    private BooleanExpression searchCondition2(ReportSearchDto reportSearchDto) {
        BooleanExpression result = null;

        if ("CATEGORY".equals(reportSearchDto.getSearchType())) {
            switch (reportSearchDto.getSearchType2()) {
                case "TITLE":
                    result = report.title.contains(reportSearchDto.getSearchKeyword2());
                    break;
                case "CONTENT":
                    result = report.content.contains(reportSearchDto.getSearchKeyword2());
                    break;
                case "CREATED_BY":
                    result = report.createdByIdx.in(JPAExpressions
                            .select(user.idx)
                            .from(user)
                            .where(user.name.contains(reportSearchDto.getSearchKeyword2())));
                    break;
                default:
                    result = null;
            }
        }

        return result;
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        report.title
                )
                .distinct().from(report)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory
                .update(report)
                .set(report.views, report.views.add(1))
                .where(report.idx.eq(idx))
                .execute();
    }
}