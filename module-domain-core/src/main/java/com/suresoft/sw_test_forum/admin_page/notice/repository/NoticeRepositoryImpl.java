package com.suresoft.sw_test_forum.admin_page.notice.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.notice.domain.Notice;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.notice.domain.QNotice.notice;
import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;

@Repository
@Transactional
public class NoticeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Notice.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<NoticeDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                NoticeDto.class,
                                notice.idx,
                                notice.createdDate,
                                notice.createdByIdx,
                                notice.activeStatus,
                                notice.views,

                                notice.title,
                                notice.priority
                        )
                )
                .from(notice)
                .where(notice.priority.loe(5))
                .orderBy(notice.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @param idx
     * @return
     */
    public Notice findAllByHighPriorityAscCheckPriority(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Notice.class,
                                notice.priority
                        )
                )
                .from(notice)
                .where(notice.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선수위 낮은 리스트 조회
     *
     * @param pageable
     * @param noticeSearchDto
     * @return
     */
    public PageImpl<NoticeDto> findAll(Pageable pageable, NoticeSearchDto noticeSearchDto) {
        JPQLQuery<NoticeDto> query = queryFactory.select(
                        Projections.bean(
                                NoticeDto.class,
                                notice.idx,
                                notice.createdDate,
                                notice.createdByIdx,
                                notice.activeStatus,
                                notice.views,

                                notice.title,
                                notice.priority
                        )
                )
                .from(notice)
                .where(searchCondition(noticeSearchDto))
                .orderBy(notice.idx.desc());

        long totalCount = query.fetchCount();
        List<NoticeDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param noticeSearchDto
     * @return
     */
    private BooleanExpression searchCondition(NoticeSearchDto noticeSearchDto) {
        BooleanExpression result;

        switch (noticeSearchDto.getSearchType()) {
            case "TITLE":
                result = notice.title.contains(noticeSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = notice.content.contains(noticeSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = notice.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(noticeSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(noticeSearchDto.getSearchKeyword())) {
            result = result.and(notice.priority.goe(6));
        }

        return result;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<NoticeDto> findTop10() {
        JPQLQuery<NoticeDto> query = queryFactory.select(
                        Projections.bean(
                                NoticeDto.class,
                                notice.idx,
                                notice.createdDate,
                                notice.createdByIdx,
                                notice.activeStatus,
                                notice.views,

                                notice.title,
                                notice.priority
                        )
                )
                .from(notice)
                .orderBy(notice.priority.asc(), notice.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        notice.title
                )
                .distinct().from(notice)
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
                .update(notice)
                .set(notice.views, notice.views.add(1))
                .where(notice.idx.eq(idx))
                .execute();
    }
}