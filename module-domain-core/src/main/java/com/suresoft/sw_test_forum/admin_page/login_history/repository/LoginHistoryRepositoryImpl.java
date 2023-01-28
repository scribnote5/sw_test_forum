package com.suresoft.sw_test_forum.admin_page.login_history.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.login_history.domain.LoginHistory;
import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistoryDto;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistorySearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.login_history.domain.QLoginHistory.loginHistory;

@Repository
@Transactional
public class LoginHistoryRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public LoginHistoryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(LoginHistory.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param loginHistorySearchDto
     * @return
     */
    public PageImpl<LoginHistoryDto> findAll(Pageable pageable, LoginHistorySearchDto loginHistorySearchDto) {
        JPQLQuery<LoginHistoryDto> query = queryFactory.select(
                        Projections.bean(
                                LoginHistoryDto.class,
                                loginHistory.idx,
                                loginHistory.createdDate,
                                loginHistory.createdByIdx,
                                loginHistory.activeStatus,
                                loginHistory.views,

                                loginHistory.loginUsername,
                                loginHistory.ip,
                                loginHistory.location,
                                loginHistory.message,
                                loginHistory.loginResultType
                        )
                )
                .from(loginHistory)
                .orderBy(loginHistory.idx.desc())
                .where(searchCondition(loginHistorySearchDto));

        long totalCount = query.fetchCount();
        List<LoginHistoryDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param loginHistorySearchDto
     * @return
     */
    private BooleanExpression searchCondition(LoginHistorySearchDto loginHistorySearchDto) {
        BooleanExpression result;

        switch (loginHistorySearchDto.getSearchType()) {
            case "USERNAME":
                result = loginHistory.loginUsername.contains(loginHistorySearchDto.getSearchKeyword());
                break;
            case "LOCATION":
                result = loginHistory.location.contains(loginHistorySearchDto.getSearchKeyword());
                break;
            case "LOGIN_RESULT_TYPE":
                result = loginHistory.loginResultType.eq(LoginResultType.valueOf(loginHistorySearchDto.getSearchKeyword()));
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
    public List<LoginHistoryDto> findTop10() {
        JPQLQuery<LoginHistoryDto> query = queryFactory.select(
                        Projections.bean(
                                LoginHistoryDto.class,
                                loginHistory.idx,
                                loginHistory.createdDate,
                                loginHistory.createdByIdx,
                                loginHistory.activeStatus,
                                loginHistory.views,

                                loginHistory.loginUsername,
                                loginHistory.ip,
                                loginHistory.location,
                                loginHistory.message,
                                loginHistory.loginResultType
                        )
                )
                .from(loginHistory)
                .orderBy(loginHistory.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public LoginHistoryDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                LoginHistoryDto.class,
                                loginHistory.idx,
                                loginHistory.createdDate,
                                loginHistory.createdByIdx,
                                loginHistory.activeStatus,
                                loginHistory.views,

                                loginHistory.loginUsername,
                                loginHistory.ip,
                                loginHistory.location,
                                loginHistory.message,
                                loginHistory.detailedMessage,
                                loginHistory.loginResultType
                        )
                )
                .from(loginHistory)
                .where(loginHistory.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(loginHistory)
                .set(loginHistory.views, loginHistory.views.add(1))
                .where(loginHistory.idx.eq(idx))
                .execute();
    }
}