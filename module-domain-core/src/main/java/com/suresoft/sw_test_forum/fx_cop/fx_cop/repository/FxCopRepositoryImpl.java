package com.suresoft.sw_test_forum.fx_cop.fx_cop.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCop;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.QFxCop.fxCop;

@Repository
@Transactional
public class FxCopRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCop.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<FxCopDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                FxCopDto.class,
                                fxCop.idx,
                                fxCop.createdDate,
                                fxCop.createdByIdx,
                                fxCop.activeStatus,
                                fxCop.views,

                                fxCop.title,
                                fxCop.priority,
                                fxCop.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(fxCop)
                .join(hashTags).on(fxCop.hashTagsIdx.eq(hashTags.idx))
                .where(fxCop.priority.loe(5))
                .orderBy(fxCop.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<FxCopDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                FxCopDto.class,
                                fxCop.priority
                        )
                )
                .from(fxCop)
                .where(fxCop.priority.loe(5))
                .orderBy(fxCop.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param fxCopSearchDto
     * @return
     */
    public PageImpl<FxCopDto> findAll(Pageable pageable, FxCopSearchDto fxCopSearchDto) {
        JPQLQuery<FxCopDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopDto.class,
                                fxCop.idx,
                                fxCop.createdDate,
                                fxCop.createdByIdx,
                                fxCop.activeStatus,
                                fxCop.views,

                                fxCop.title,
                                fxCop.priority,
                                fxCop.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(fxCop)
                .join(hashTags).on(fxCop.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(fxCopSearchDto))
                .orderBy(fxCop.idx.desc());

        long totalCount = query.fetchCount();
        List<FxCopDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param fxCopSearchDto
     * @return
     */
    private BooleanExpression searchCondition(FxCopSearchDto fxCopSearchDto) {
        BooleanExpression result;

        switch (fxCopSearchDto.getSearchType()) {
            case "TITLE":
                result = fxCop.title.contains(fxCopSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(fxCopSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = fxCop.content.contains(fxCopSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = fxCop.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(fxCopSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(fxCopSearchDto.getSearchKeyword())) {
            result = result.and(fxCop.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public FxCopDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopDto.class,
                                fxCop.idx,
                                fxCop.createdDate,
                                fxCop.createdByIdx,
                                fxCop.lastModifiedDate,
                                fxCop.lastModifiedByIdx,
                                fxCop.activeStatus,
                                fxCop.views,

                                fxCop.title,
                                fxCop.priority,
                                fxCop.frequency,
                                hashTags.content.as("hashTags"),
                                fxCop.hashTagsIdx,
                                fxCop.category,
                                fxCop.breakingChange,
                                fxCop.content
                        )
                )
                .from(fxCop)
                .join(hashTags).on(fxCop.hashTagsIdx.eq(hashTags.idx))
                .where(fxCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        fxCop.title
                )
                .distinct().from(fxCop)
                .fetch();
    }

    /**
     * FxCop 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public FxCop findFxCopByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCop.class,
                                fxCop.title,
                                fxCop.priority
                        )
                )
                .from(fxCop)
                .where(fxCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public FxCop findFxCopPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCop.class,
                                fxCop.priority
                        )
                )
                .from(fxCop)
                .where(fxCop.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(fxCop)
                .set(fxCop.views, fxCop.views.add(1))
                .where(fxCop.idx.eq(idx))
                .execute();
    }
}