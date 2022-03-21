package com.suresoft.sw_test_forum.admin_page.data_history.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistoryDto;
import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistorySearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.data_history.domain.QDataHistory.dataHistory;
import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;

@Repository
@Transactional
public class DataHistoryRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DataHistoryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DataHistory.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param dataHistorySearchDto
     * @return
     */
    public PageImpl<DataHistoryDto> findAll(Pageable pageable, DataHistorySearchDto dataHistorySearchDto) {
        JPQLQuery<DataHistoryDto> query = queryFactory.select(
                        Projections.bean(
                                DataHistoryDto.class,
                                dataHistory.idx,
                                dataHistory.createdDate,
                                dataHistory.createdByIdx,
                                dataHistory.activeStatus,
                                dataHistory.views,

                                dataHistory.auditIdx,
                                dataHistory.auditBoard,
                                dataHistory.auditType,
                                dataHistory.message
                        )
                )
                .from(dataHistory)
                .orderBy(dataHistory.idx.desc())
                .where(searchCondition(dataHistorySearchDto));

        long totalCount = query.fetchCount();
        List<DataHistoryDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param dataHistorySearchDto
     * @return
     */
    private BooleanExpression searchCondition(DataHistorySearchDto dataHistorySearchDto) {
        BooleanExpression result;

        switch (dataHistorySearchDto.getSearchType()) {
            case "MESSAGE":
                result = dataHistory.message.contains(dataHistorySearchDto.getSearchKeyword());
                break;
            case "AUDIT_BOARD":
                result = dataHistory.auditBoard.contains(dataHistorySearchDto.getSearchKeyword());
                break;
            case "AUDIT_TYPE":
                result = dataHistory.auditType.eq(AuditType.valueOf(dataHistorySearchDto.getSearchKeyword()));
                break;
            case "CONTENT":
                result = dataHistory.content.contains(dataHistorySearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = dataHistory.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(dataHistorySearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<DataHistoryDto> findTop10() {
        JPQLQuery<DataHistoryDto> query = queryFactory.select(
                        Projections.bean(
                                DataHistoryDto.class,
                                dataHistory.idx,
                                dataHistory.createdDate,
                                dataHistory.createdByIdx,
                                dataHistory.activeStatus,
                                dataHistory.views,

                                dataHistory.auditIdx,
                                dataHistory.auditBoard,
                                dataHistory.auditType,
                                dataHistory.message
                        )
                )
                .from(dataHistory)
                .orderBy(dataHistory.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public DataHistoryDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                DataHistoryDto.class,
                                dataHistory.idx,
                                dataHistory.createdDate,
                                dataHistory.createdByIdx,
                                dataHistory.activeStatus,
                                dataHistory.views,

                                dataHistory.auditIdx,
                                dataHistory.auditBoard,
                                dataHistory.auditType,
                                dataHistory.message,
                                dataHistory.detailedMessage,
                                dataHistory.content
                        )
                )
                .from(dataHistory)
                .where(dataHistory.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(dataHistory)
                .set(dataHistory.views, dataHistory.views.add(1))
                .where(dataHistory.idx.eq(idx))
                .execute();
    }
}