package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCop;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.style_cop.style_cop.domain.QStyleCop.styleCop;

@Repository
@Transactional
public class StyleCopRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCop.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<StyleCopDto> findTop10ByViews() {
        JPQLQuery<StyleCopDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopDto.class,
                                styleCop.idx,
                                styleCop.createdDate,
                                styleCop.createdByIdx,
                                styleCop.activeStatus,
                                styleCop.views,

                                styleCop.title,
                                styleCop.priority,
                                styleCop.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(styleCop)
                .join(hashTags).on(styleCop.hashTagsIdx.eq(hashTags.idx))
                .orderBy(styleCop.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StyleCopDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopDto.class,
                                styleCop.idx,
                                styleCop.createdDate,
                                styleCop.createdByIdx,
                                styleCop.activeStatus,
                                styleCop.views,

                                styleCop.title,
                                styleCop.priority,
                                styleCop.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(styleCop)
                .join(hashTags).on(styleCop.hashTagsIdx.eq(hashTags.idx))
                .where(styleCop.priority.loe(5))
                .orderBy(styleCop.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<StyleCopDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopDto.class,
                                styleCop.priority
                        )
                )
                .from(styleCop)
                .where(styleCop.priority.loe(5))
                .orderBy(styleCop.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param styleCopSearchDto
     * @return
     */
    public PageImpl<StyleCopDto> findAll(Pageable pageable, StyleCopSearchDto styleCopSearchDto) {
        JPQLQuery<StyleCopDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopDto.class,
                                styleCop.idx,
                                styleCop.createdDate,
                                styleCop.createdByIdx,
                                styleCop.activeStatus,
                                styleCop.views,

                                styleCop.title,
                                styleCop.priority,
                                styleCop.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(styleCop)
                .join(hashTags).on(styleCop.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(styleCopSearchDto))
                .orderBy(styleCop.idx.desc());

        long totalCount = query.fetchCount();
        List<StyleCopDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param styleCopSearchDto
     * @return
     */
    private BooleanExpression searchCondition(StyleCopSearchDto styleCopSearchDto) {
        BooleanExpression result;

        switch (styleCopSearchDto.getSearchType()) {
            case "TITLE":
                result = styleCop.title.contains(styleCopSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(styleCopSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = styleCop.content.contains(styleCopSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = styleCop.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(styleCopSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(styleCopSearchDto.getSearchKeyword())) {
            result = result.and(styleCop.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StyleCopDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopDto.class,
                                styleCop.idx,
                                styleCop.createdDate,
                                styleCop.createdByIdx,
                                styleCop.lastModifiedDate,
                                styleCop.lastModifiedByIdx,
                                styleCop.activeStatus,
                                styleCop.views,

                                styleCop.title,
                                styleCop.priority,
                                styleCop.frequency,
                                hashTags.content.as("hashTags"),
                                styleCop.hashTagsIdx,
                                styleCop.category,
                                styleCop.content
                        )
                )
                .from(styleCop)
                .join(hashTags).on(styleCop.hashTagsIdx.eq(hashTags.idx))
                .where(styleCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        styleCop.title
                )
                .distinct().from(styleCop)
                .fetch();
    }

    /**
     * StyleCop 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public StyleCop findStyleCopByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCop.class,
                                styleCop.title,
                                styleCop.priority
                        )
                )
                .from(styleCop)
                .where(styleCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public StyleCop findStyleCopPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCop.class,
                                styleCop.priority
                        )
                )
                .from(styleCop)
                .where(styleCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(styleCop)
                .set(styleCop.views, styleCop.views.add(1))
                .where(styleCop.idx.eq(idx))
                .execute();
    }
}