package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExample;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.QJavaCodeConventionsExample.javaCodeConventionsExample;

@Repository
@Transactional
public class JavaCodeConventionsExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<JavaCodeConventionsExample> findAllByHighPriorityAsc(long javaCodeConventionsIdx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExample.class,
                                javaCodeConventionsExample.idx,
                                javaCodeConventionsExample.createdDate,
                                javaCodeConventionsExample.createdByIdx,
                                javaCodeConventionsExample.activeStatus,
                                javaCodeConventionsExample.views,

                                javaCodeConventionsExample.title,
                                javaCodeConventionsExample.priority
                        )
                )
                .from(javaCodeConventionsExample)
                .where(javaCodeConventionsExample.priority.loe(3)
                        .and(javaCodeConventionsExample.javaCodeConventionsIdx.eq(javaCodeConventionsIdx)))
                .orderBy(javaCodeConventionsExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public JavaCodeConventionsExample findAllPriorityByIdx(long idx, long javaCodeConventionsIdx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExample.class,
                                javaCodeConventionsExample.priority
                        )
                )
                .from(javaCodeConventionsExample)
                .where(javaCodeConventionsExample.idx.eq(idx)
                        .and(javaCodeConventionsExample.javaCodeConventionsIdx.eq(javaCodeConventionsIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsExampleSearchDto
     * @return
     */
    public PageImpl<JavaCodeConventionsExampleDto> findAll(Pageable pageable, JavaCodeConventionsExampleSearchDto javaCodeConventionsExampleSearchDto) {
        JPQLQuery<JavaCodeConventionsExampleDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExampleDto.class,
                                javaCodeConventionsExample.idx,
                                javaCodeConventionsExample.createdDate,
                                javaCodeConventionsExample.createdByIdx,
                                javaCodeConventionsExample.activeStatus,
                                javaCodeConventionsExample.views,

                                javaCodeConventionsExample.javaCodeConventionsIdx,
                                javaCodeConventionsExample.title,
                                javaCodeConventionsExample.priority
                        )
                )
                .from(javaCodeConventionsExample)
                .where(searchCondition(javaCodeConventionsExampleSearchDto),
                        searchCondition(javaCodeConventionsExampleSearchDto.getJavaCodeConventionsIdx()))
                .orderBy(javaCodeConventionsExample.priority.asc());

        long totalCount = query.fetchCount();
        List<JavaCodeConventionsExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param javaCodeConventionsExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(JavaCodeConventionsExampleSearchDto javaCodeConventionsExampleSearchDto) {
        BooleanExpression result;

        switch (javaCodeConventionsExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = javaCodeConventionsExample.title.contains(javaCodeConventionsExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = javaCodeConventionsExample.content.contains(javaCodeConventionsExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = javaCodeConventionsExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(javaCodeConventionsExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public List<JavaCodeConventionsExampleDto> findAll(long javaCodeConventionsIdx) {
        JPQLQuery<JavaCodeConventionsExampleDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExampleDto.class,
                                javaCodeConventionsExample.idx,
                                javaCodeConventionsExample.createdDate,
                                javaCodeConventionsExample.createdByIdx,
                                javaCodeConventionsExample.activeStatus,
                                javaCodeConventionsExample.views,

                                javaCodeConventionsExample.javaCodeConventionsIdx,
                                javaCodeConventionsExample.title,
                                javaCodeConventionsExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                javaCodeConventionsExample.content,
                                javaCodeConventionsExample.nonCompliantExample,
                                javaCodeConventionsExample.compliantExample,
                                javaCodeConventionsExample.badCasePositionList,
                                javaCodeConventionsExample.goodCasePositionList
                        )
                )
                .from(javaCodeConventionsExample)
                .join(toolInformation).on(javaCodeConventionsExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(javaCodeConventionsExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(javaCodeConventionsExample.javaCodeConventionsIdx.eq(javaCodeConventionsIdx)
                        .and(javaCodeConventionsExample.priority.loe(3)))
                .orderBy(javaCodeConventionsExample.priority.asc(), javaCodeConventionsExample.idx.desc());

        return query.fetch();
    }

    /**
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    private BooleanExpression searchCondition(long javaCodeConventionsIdx) {
        BooleanExpression result;

        if (javaCodeConventionsIdx == 0) {
            result = null;
        } else {
            result = javaCodeConventionsExample.javaCodeConventionsIdx.eq(javaCodeConventionsIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExampleDto.class,
                                javaCodeConventionsExample.idx,
                                javaCodeConventionsExample.createdDate,
                                javaCodeConventionsExample.createdByIdx,
                                javaCodeConventionsExample.lastModifiedDate,
                                javaCodeConventionsExample.lastModifiedByIdx,
                                javaCodeConventionsExample.activeStatus,
                                javaCodeConventionsExample.views,

                                javaCodeConventionsExample.javaCodeConventionsIdx,
                                javaCodeConventionsExample.title,
                                javaCodeConventionsExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                javaCodeConventionsExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                javaCodeConventionsExample.compilerInformationIdx,
                                javaCodeConventionsExample.content,
                                javaCodeConventionsExample.nonCompliantExample,
                                javaCodeConventionsExample.compliantExample,
                                javaCodeConventionsExample.badCasePositionList,
                                javaCodeConventionsExample.goodCasePositionList
                        )
                )
                .from(javaCodeConventionsExample)
                .join(toolInformation).on(javaCodeConventionsExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(javaCodeConventionsExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(javaCodeConventionsExample.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        /*
         * SELECT
         *  distinct javaCodeConventions.title
         *
         */
        return queryFactory.select(
                        javaCodeConventionsExample.title
                )
                .distinct().from(javaCodeConventionsExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(javaCodeConventionsExample)
                .set(javaCodeConventionsExample.views, javaCodeConventionsExample.views.add(1))
                .where(javaCodeConventionsExample.idx.eq(idx))
                .execute();
    }

    /**
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public List<JavaCodeConventionsExampleDto> findAllWhenDelete(long javaCodeConventionsIdx) {
        JPQLQuery<JavaCodeConventionsExampleDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsExampleDto.class,
                                javaCodeConventionsExample.idx
                        )
                )
                .from(javaCodeConventionsExample)
                .where(javaCodeConventionsExample.javaCodeConventionsIdx.eq(javaCodeConventionsIdx));

        return query.fetch();
    }
}