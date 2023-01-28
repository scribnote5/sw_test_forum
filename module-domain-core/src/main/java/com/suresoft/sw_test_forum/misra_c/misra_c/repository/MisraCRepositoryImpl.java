package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraC;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.misra_c.misra_c.domain.QMisraC.misraC;

@Repository
@Transactional
public class MisraCRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraC.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCDto> findTop10ByViews() {
        JPQLQuery<MisraCDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCDto.class,
                                misraC.idx,
                                misraC.createdDate,
                                misraC.createdByIdx,
                                misraC.activeStatus,
                                misraC.views,

                                misraC.title,
                                misraC.priority,
                                misraC.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraC)
                .join(hashTags).on(misraC.hashTagsIdx.eq(hashTags.idx))
                .orderBy(misraC.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                MisraCDto.class,
                                misraC.idx,
                                misraC.createdDate,
                                misraC.createdByIdx,
                                misraC.activeStatus,
                                misraC.views,

                                misraC.title,
                                misraC.priority,
                                misraC.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraC)
                .join(hashTags).on(misraC.hashTagsIdx.eq(hashTags.idx))
                .where(misraC.priority.loe(5))
                .orderBy(misraC.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                MisraCDto.class,
                                misraC.priority
                        )
                )
                .from(misraC)
                .where(misraC.priority.loe(5))
                .orderBy(misraC.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param misraCSearchDto
     * @return
     */
    public PageImpl<MisraCDto> findAll(Pageable pageable, MisraCSearchDto misraCSearchDto) {
        JPQLQuery<MisraCDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCDto.class,
                                misraC.idx,
                                misraC.createdDate,
                                misraC.createdByIdx,
                                misraC.activeStatus,
                                misraC.views,

                                misraC.title,
                                misraC.priority,
                                misraC.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraC)
                .join(hashTags).on(misraC.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(misraCSearchDto))
                .orderBy(misraC.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCSearchDto misraCSearchDto) {
        BooleanExpression result;

        switch (misraCSearchDto.getSearchType()) {
            case "TITLE":
                result = misraC.title.contains(misraCSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(misraCSearchDto.getSearchKeyword());
                break;
            case "QAC_TITLE":
                result = misraC.qacTitle.contains(misraCSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraC.content.contains(misraCSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraC.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(misraCSearchDto.getSearchKeyword())) {
            result = result.and(misraC.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCDto.class,
                                misraC.idx,
                                misraC.createdDate,
                                misraC.createdByIdx,
                                misraC.lastModifiedDate,
                                misraC.lastModifiedByIdx,
                                misraC.activeStatus,
                                misraC.views,

                                misraC.title,
                                misraC.originalTitle,
                                misraC.priority,
                                misraC.frequency,
                                hashTags.content.as("hashTags"),
                                misraC.hashTagsIdx,
                                misraC.category,
                                misraC.scope,
                                misraC.decidability,
                                misraC.appliesTo,
                                misraC.qacTitle,
                                misraC.content
                        )
                )
                .from(misraC)
                .join(hashTags).on(misraC.hashTagsIdx.eq(hashTags.idx))
                .where(misraC.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        misraC.title
                )
                .distinct().from(misraC)
                .fetch();
    }

    /**
     * MISRA C 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public MisraC findMisraCByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraC.class,
                                misraC.title,
                                misraC.priority
                        )
                )
                .from(misraC)
                .where(misraC.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public MisraC findMisraCPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraC.class,
                                misraC.priority
                        )
                )
                .from(misraC)
                .where(misraC.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraC)
                .set(misraC.views, misraC.views.add(1))
                .where(misraC.idx.eq(idx))
                .execute();
    }
}