package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCpp;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.QMisraCpp.misraCpp;

@Repository
@Transactional
public class MisraCppRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCpp.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCppDto> findTop10ByViews() {
        JPQLQuery<MisraCppDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppDto.class,
                                misraCpp.idx,
                                misraCpp.createdDate,
                                misraCpp.createdByIdx,
                                misraCpp.activeStatus,
                                misraCpp.views,

                                misraCpp.title,
                                misraCpp.priority,
                                misraCpp.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraCpp)
                .join(hashTags).on(misraCpp.hashTagsIdx.eq(hashTags.idx))
                .orderBy(misraCpp.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCppDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppDto.class,
                                misraCpp.idx,
                                misraCpp.createdDate,
                                misraCpp.createdByIdx,
                                misraCpp.activeStatus,
                                misraCpp.views,

                                misraCpp.title,
                                misraCpp.priority,
                                misraCpp.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraCpp)
                .join(hashTags).on(misraCpp.hashTagsIdx.eq(hashTags.idx))
                .where(misraCpp.priority.loe(5))
                .orderBy(misraCpp.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCppDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppDto.class,
                                misraCpp.priority
                        )
                )
                .from(misraCpp)
                .where(misraCpp.priority.loe(5))
                .orderBy(misraCpp.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param misraCppSearchDto
     * @return
     */
    public PageImpl<MisraCppDto> findAll(Pageable pageable, MisraCppSearchDto misraCppSearchDto) {
        JPQLQuery<MisraCppDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppDto.class,
                                misraCpp.idx,
                                misraCpp.createdDate,
                                misraCpp.createdByIdx,
                                misraCpp.activeStatus,
                                misraCpp.views,

                                misraCpp.title,
                                misraCpp.priority,
                                misraCpp.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(misraCpp)
                .join(hashTags).on(misraCpp.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(misraCppSearchDto))
                .orderBy(misraCpp.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCppDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCppSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCppSearchDto misraCppSearchDto) {
        BooleanExpression result;

        switch (misraCppSearchDto.getSearchType()) {
            case "TITLE":
                result = misraCpp.title.contains(misraCppSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(misraCppSearchDto.getSearchKeyword());
                break;
            case "QAC_TITLE":
                result = misraCpp.qacTitle.contains(misraCppSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraCpp.content.contains(misraCppSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraCpp.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCppSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(misraCppSearchDto.getSearchKeyword())) {
            result = result.and(misraCpp.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCppDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppDto.class,
                                misraCpp.idx,
                                misraCpp.createdDate,
                                misraCpp.createdByIdx,
                                misraCpp.lastModifiedDate,
                                misraCpp.lastModifiedByIdx,
                                misraCpp.activeStatus,
                                misraCpp.views,

                                misraCpp.title,
                                misraCpp.originalTitle,
                                misraCpp.priority,
                                misraCpp.frequency,
                                hashTags.content.as("hashTags"),
                                misraCpp.hashTagsIdx,
                                misraCpp.category,
                                misraCpp.qacTitle,
                                misraCpp.content
                        )
                )
                .from(misraCpp)
                .join(hashTags).on(misraCpp.hashTagsIdx.eq(hashTags.idx))
                .where(misraCpp.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        misraCpp.title
                )
                .distinct().from(misraCpp)
                .fetch();
    }

    /**
     * MISRA C 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public MisraCpp findMisraCppByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCpp.class,
                                misraCpp.title,
                                misraCpp.priority
                        )
                )
                .from(misraCpp)
                .where(misraCpp.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public MisraCpp findMisraCppPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCpp.class,
                                misraCpp.priority
                        )
                )
                .from(misraCpp)
                .where(misraCpp.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraCpp)
                .set(misraCpp.views, misraCpp.views.add(1))
                .where(misraCpp.idx.eq(idx))
                .execute();
    }
}