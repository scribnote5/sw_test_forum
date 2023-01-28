package com.suresoft.sw_test_forum.cwe.cwe.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe.domain.Cwe;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.cwe.cwe.domain.QCwe.cwe;

@Repository
@Transactional
public class CweRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Cwe.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<CweDto> findTop10ByViews() {
        JPQLQuery<CweDto> query = queryFactory.select(
                        Projections.bean(
                                CweDto.class,
                                cwe.idx,
                                cwe.createdDate,
                                cwe.createdByIdx,
                                cwe.activeStatus,
                                cwe.views,

                                cwe.title,
                                cwe.priority,
                                cwe.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(cwe)
                .join(hashTags).on(cwe.hashTagsIdx.eq(hashTags.idx))
                .orderBy(cwe.views.desc())
                .limit(10);

        return query.fetch();
    }


    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                CweDto.class,
                                cwe.idx,
                                cwe.createdDate,
                                cwe.createdByIdx,
                                cwe.activeStatus,
                                cwe.views,

                                cwe.title,
                                cwe.priority,
                                cwe.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(cwe)
                .join(hashTags).on(cwe.hashTagsIdx.eq(hashTags.idx))
                .where(cwe.priority.loe(5))
                .orderBy(cwe.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<CweDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                CweDto.class,
                                cwe.priority
                        )
                )
                .from(cwe)
                .where(cwe.priority.loe(5))
                .orderBy(cwe.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param cweSearchDto
     * @return
     */
    public PageImpl<CweDto> findAll(Pageable pageable, CweSearchDto cweSearchDto) {
        JPQLQuery<CweDto> query = queryFactory.select(
                        Projections.bean(
                                CweDto.class,
                                cwe.idx,
                                cwe.createdDate,
                                cwe.createdByIdx,
                                cwe.activeStatus,
                                cwe.views,

                                cwe.title,
                                cwe.priority,
                                cwe.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(cwe)
                .join(hashTags).on(cwe.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(cweSearchDto))
                .orderBy(cwe.idx.desc());

        long totalCount = query.fetchCount();
        List<CweDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweSearchDto cweSearchDto) {
        BooleanExpression result;

        switch (cweSearchDto.getSearchType()) {
            case "TITLE":
                result = cwe.title.contains(cweSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(cweSearchDto.getSearchKeyword());
                break;
            case "STATIC_TITLE":
                result = cwe.staticTitle.contains(cweSearchDto.getSearchKeyword());
                break;
            case "CODE_SONAR_TITLE":
                result = cwe.codeSonarTitle.contains(cweSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cwe.content.contains(cweSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cwe.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(cweSearchDto.getSearchKeyword())) {
            result = result.and(cwe.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweDto.class,
                                cwe.idx,
                                cwe.createdDate,
                                cwe.createdByIdx,
                                cwe.lastModifiedDate,
                                cwe.lastModifiedByIdx,
                                cwe.activeStatus,
                                cwe.views,

                                cwe.title,
                                cwe.priority,
                                cwe.frequency,
                                hashTags.content.as("hashTags"),
                                cwe.hashTagsIdx,
                                cwe.staticTitle,
                                cwe.codeSonarTitle,
                                cwe.content
                        )
                )
                .from(cwe)
                .join(hashTags).on(cwe.hashTagsIdx.eq(hashTags.idx))
                .where(cwe.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        cwe.title
                )
                .distinct().from(cwe)
                .fetch();
    }

    /**
     * CWE 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public Cwe findCweByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Cwe.class,
                                cwe.title,
                                cwe.priority
                        )
                )
                .from(cwe)
                .where(cwe.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public Cwe findCwePriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Cwe.class,
                                cwe.priority
                        )
                )
                .from(cwe)
                .where(cwe.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cwe)
                .set(cwe.views, cwe.views.add(1))
                .where(cwe.idx.eq(idx))
                .execute();
    }
}