package com.suresoft.sw_test_forum.cwe_java.cwe_java.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJava;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.QCweJava.cweJava;

@Repository
@Transactional
public class CweJavaRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJava.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweJavaDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaDto.class,
                                cweJava.idx,
                                cweJava.createdDate,
                                cweJava.createdByIdx,
                                cweJava.activeStatus,
                                cweJava.views,

                                cweJava.title,
                                cweJava.priority,
                                cweJava.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(cweJava)
                .join(hashTags).on(cweJava.hashTagsIdx.eq(hashTags.idx))
                .where(cweJava.priority.loe(5))
                .orderBy(cweJava.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<CweJavaDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaDto.class,
                                cweJava.priority
                        )
                )
                .from(cweJava)
                .where(cweJava.priority.loe(5))
                .orderBy(cweJava.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param cweJavaSearchDto
     * @return
     */
    public PageImpl<CweJavaDto> findAll(Pageable pageable, CweJavaSearchDto cweJavaSearchDto) {
        JPQLQuery<CweJavaDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaDto.class,
                                cweJava.idx,
                                cweJava.createdDate,
                                cweJava.createdByIdx,
                                cweJava.activeStatus,
                                cweJava.views,

                                cweJava.title,
                                cweJava.priority,
                                cweJava.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(cweJava)
                .join(hashTags).on(cweJava.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(cweJavaSearchDto))
                .orderBy(cweJava.idx.desc());

        long totalCount = query.fetchCount();
        List<CweJavaDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweJavaSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweJavaSearchDto cweJavaSearchDto) {
        BooleanExpression result;

        switch (cweJavaSearchDto.getSearchType()) {
            case "TITLE":
                result = cweJava.title.contains(cweJavaSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(cweJavaSearchDto.getSearchKeyword());
                break;
            case "STATIC_TITLE":
                result = cweJava.staticTitle.contains(cweJavaSearchDto.getSearchKeyword());
                break;
            case "SPARROW_TITLE":
                result = cweJava.sparrowTitle.contains(cweJavaSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cweJava.content.contains(cweJavaSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cweJava.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweJavaSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(cweJavaSearchDto.getSearchKeyword())) {
            result = result.and(cweJava.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaDto.class,
                                cweJava.idx,
                                cweJava.createdDate,
                                cweJava.createdByIdx,
                                cweJava.lastModifiedDate,
                                cweJava.lastModifiedByIdx,
                                cweJava.activeStatus,
                                cweJava.views,

                                cweJava.title,
                                cweJava.originalTitle,
                                cweJava.priority,
                                cweJava.frequency,
                                hashTags.content.as("hashTags"),
                                cweJava.hashTagsIdx,
                                cweJava.staticTitle,
                                cweJava.sparrowTitle,
                                cweJava.content
                        )
                )
                .from(cweJava)
                .join(hashTags).on(cweJava.hashTagsIdx.eq(hashTags.idx))
                .where(cweJava.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        cweJava.title
                )
                .distinct().from(cweJava)
                .fetch();
    }

    /**
     * CWE 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public CweJava findCweJavaByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJava.class,
                                cweJava.title,
                                cweJava.priority
                        )
                )
                .from(cweJava)
                .where(cweJava.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public CweJava findCweJavaPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJava.class,
                                cweJava.priority
                        )
                )
                .from(cweJava)
                .where(cweJava.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cweJava)
                .set(cweJava.views, cweJava.views.add(1))
                .where(cweJava.idx.eq(idx))
                .execute();
    }
}