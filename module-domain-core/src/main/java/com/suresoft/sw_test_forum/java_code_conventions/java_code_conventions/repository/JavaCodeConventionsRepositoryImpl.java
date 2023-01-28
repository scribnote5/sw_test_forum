package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventions;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.QJavaCodeConventions.javaCodeConventions;

@Repository
@Transactional
public class JavaCodeConventionsRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventions.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<JavaCodeConventionsDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsDto.class,
                                javaCodeConventions.idx,
                                javaCodeConventions.createdDate,
                                javaCodeConventions.createdByIdx,
                                javaCodeConventions.activeStatus,
                                javaCodeConventions.views,

                                javaCodeConventions.title,
                                javaCodeConventions.priority,
                                javaCodeConventions.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(javaCodeConventions)
                .join(hashTags).on(javaCodeConventions.hashTagsIdx.eq(hashTags.idx))
                .where(javaCodeConventions.priority.loe(5))
                .orderBy(javaCodeConventions.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<JavaCodeConventionsDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsDto.class,
                                javaCodeConventions.priority
                        )
                )
                .from(javaCodeConventions)
                .where(javaCodeConventions.priority.loe(5))
                .orderBy(javaCodeConventions.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsSearchDto
     * @return
     */
    public PageImpl<JavaCodeConventionsDto> findAll(Pageable pageable, JavaCodeConventionsSearchDto javaCodeConventionsSearchDto) {
        JPQLQuery<JavaCodeConventionsDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsDto.class,
                                javaCodeConventions.idx,
                                javaCodeConventions.createdDate,
                                javaCodeConventions.createdByIdx,
                                javaCodeConventions.activeStatus,
                                javaCodeConventions.views,

                                javaCodeConventions.title,
                                javaCodeConventions.priority,
                                javaCodeConventions.frequency,
                                hashTags.content.as("hashTags")
                        )
                )
                .from(javaCodeConventions)
                .join(hashTags).on(javaCodeConventions.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(javaCodeConventionsSearchDto))
                .orderBy(javaCodeConventions.idx.desc());

        long totalCount = query.fetchCount();
        List<JavaCodeConventionsDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param javaCodeConventionsSearchDto
     * @return
     */
    private BooleanExpression searchCondition(JavaCodeConventionsSearchDto javaCodeConventionsSearchDto) {
        BooleanExpression result;

        switch (javaCodeConventionsSearchDto.getSearchType()) {
            case "TITLE":
                result = javaCodeConventions.title.contains(javaCodeConventionsSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(javaCodeConventionsSearchDto.getSearchKeyword());
                break;
            case "STATIC_TITLE":
                result = javaCodeConventions.staticTitle.contains(javaCodeConventionsSearchDto.getSearchKeyword());
                break;
            case "SPARROW_TITLE":
                result = javaCodeConventions.sparrowTitle.contains(javaCodeConventionsSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = javaCodeConventions.content.contains(javaCodeConventionsSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = javaCodeConventions.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(javaCodeConventionsSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(javaCodeConventionsSearchDto.getSearchKeyword())) {
            result = result.and(javaCodeConventions.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsDto.class,
                                javaCodeConventions.idx,
                                javaCodeConventions.createdDate,
                                javaCodeConventions.createdByIdx,
                                javaCodeConventions.lastModifiedDate,
                                javaCodeConventions.lastModifiedByIdx,
                                javaCodeConventions.activeStatus,
                                javaCodeConventions.views,

                                javaCodeConventions.title,
                                javaCodeConventions.originalTitle,
                                javaCodeConventions.priority,
                                javaCodeConventions.frequency,
                                hashTags.content.as("hashTags"),
                                javaCodeConventions.hashTagsIdx,
                                javaCodeConventions.staticTitle,
                                javaCodeConventions.sparrowTitle,
                                javaCodeConventions.content
                        )
                )
                .from(javaCodeConventions)
                .join(hashTags).on(javaCodeConventions.hashTagsIdx.eq(hashTags.idx))
                .where(javaCodeConventions.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 규칙명 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        javaCodeConventions.title
                )
                .distinct().from(javaCodeConventions)
                .fetch();
    }

    /**
     * JAVA CODE CONVENTIONS 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventions findjavaCodeConventionsByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventions.class,
                                javaCodeConventions.title,
                                javaCodeConventions.priority
                        )
                )
                .from(javaCodeConventions)
                .where(javaCodeConventions.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventions findjavaCodeConventionsPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventions.class,
                                javaCodeConventions.priority
                        )
                )
                .from(javaCodeConventions)
                .where(javaCodeConventions.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(javaCodeConventions)
                .set(javaCodeConventions.views, javaCodeConventions.views.add(1))
                .where(javaCodeConventions.idx.eq(idx))
                .execute();
    }
}