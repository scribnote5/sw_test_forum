package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.common.domain.QProjectInformation.projectInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.QJavaCodeConventionsGuideline.javaCodeConventionsGuideline;
import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.QJavaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineLike;

@Repository
@Transactional
public class JavaCodeConventionsGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsGuidelineSearchDto
     * @return
     */
    public PageImpl<JavaCodeConventionsGuidelineDto> findAll(Pageable pageable, JavaCodeConventionsGuidelineSearchDto javaCodeConventionsGuidelineSearchDto) {
        JPQLQuery<JavaCodeConventionsGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsGuidelineDto.class,
                                javaCodeConventionsGuideline.idx,
                                javaCodeConventionsGuideline.createdDate,
                                javaCodeConventionsGuideline.createdByIdx,
                                javaCodeConventionsGuideline.activeStatus,
                                javaCodeConventionsGuideline.views,

                                javaCodeConventionsGuideline.javaCodeConventionsIdx,
                                javaCodeConventionsGuideline.title,
                                hashTags.content.as("hashTags"),
                                javaCodeConventionsGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(javaCodeConventionsGuidelineLike.idx.count())
                                        .from(javaCodeConventionsGuidelineLike)
                                        .where(javaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuideline.idx)), "likeCountInList")
                        )
                )
                .from(javaCodeConventionsGuideline)
                .join(hashTags).on(javaCodeConventionsGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(javaCodeConventionsGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(javaCodeConventionsGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(javaCodeConventionsGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(javaCodeConventionsGuidelineSearchDto),
                        searchCondition(javaCodeConventionsGuidelineSearchDto.getJavaCodeConventionsIdx()))
                .orderBy(javaCodeConventionsGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<JavaCodeConventionsGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param javaCodeConventionsGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(JavaCodeConventionsGuidelineSearchDto javaCodeConventionsGuidelineSearchDto) {
        BooleanExpression result;

        switch (javaCodeConventionsGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = javaCodeConventionsGuideline.title.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = javaCodeConventionsGuideline.guidelineResult.eq(GuidelineResult.valueOf(javaCodeConventionsGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = javaCodeConventionsGuideline.content.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = javaCodeConventionsGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(javaCodeConventionsGuidelineSearchDto.getSearchKeyword())));
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
    public List<JavaCodeConventionsGuidelineDto> findAll(long javaCodeConventionsIdx) {
        JPQLQuery<JavaCodeConventionsGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsGuidelineDto.class,
                                javaCodeConventionsGuideline.idx,
                                javaCodeConventionsGuideline.createdDate,
                                javaCodeConventionsGuideline.createdByIdx,
                                javaCodeConventionsGuideline.activeStatus,
                                javaCodeConventionsGuideline.views,

                                javaCodeConventionsGuideline.title,
                                hashTags.content.as("hashTags"),
                                javaCodeConventionsGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(javaCodeConventionsGuidelineLike.idx.count())
                                        .from(javaCodeConventionsGuidelineLike)
                                        .where(javaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuideline.idx)), "likeCountInList")
                        )
                )
                .from(javaCodeConventionsGuideline)
                .join(hashTags).on(javaCodeConventionsGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(javaCodeConventionsGuideline.javaCodeConventionsIdx.eq(javaCodeConventionsIdx))
                .orderBy(javaCodeConventionsGuideline.idx.desc())
                .limit(10);

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
            result = javaCodeConventionsGuideline.javaCodeConventionsIdx.eq(javaCodeConventionsIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsGuidelineDto.class,
                                javaCodeConventionsGuideline.idx,
                                javaCodeConventionsGuideline.createdDate,
                                javaCodeConventionsGuideline.createdByIdx,
                                javaCodeConventionsGuideline.lastModifiedDate,
                                javaCodeConventionsGuideline.lastModifiedByIdx,
                                javaCodeConventionsGuideline.activeStatus,
                                javaCodeConventionsGuideline.views,

                                javaCodeConventionsGuideline.javaCodeConventionsIdx,
                                javaCodeConventionsGuideline.title,
                                hashTags.content.as("hashTags"),
                                javaCodeConventionsGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                javaCodeConventionsGuideline.projectInformationIdx,
                                javaCodeConventionsGuideline.guidelineResult,
                                javaCodeConventionsGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                javaCodeConventionsGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                javaCodeConventionsGuideline.compilerInformationIdx,
                                javaCodeConventionsGuideline.content,
                                javaCodeConventionsGuideline.nonCompliantExample,
                                javaCodeConventionsGuideline.compliantExample,
                                javaCodeConventionsGuideline.badCasePositionList,
                                javaCodeConventionsGuideline.goodCasePositionList
                        )
                )
                .from(javaCodeConventionsGuideline)
                .join(hashTags).on(javaCodeConventionsGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(javaCodeConventionsGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(javaCodeConventionsGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(javaCodeConventionsGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(javaCodeConventionsGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        javaCodeConventionsGuideline.title
                )
                .distinct().from(javaCodeConventionsGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(javaCodeConventionsGuideline)
                .set(javaCodeConventionsGuideline.views, javaCodeConventionsGuideline.views.add(1))
                .where(javaCodeConventionsGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public List<JavaCodeConventionsGuidelineDto> findAllWhenDelete(long javaCodeConventionsIdx) {
        JPQLQuery<JavaCodeConventionsGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsGuidelineDto.class,
                                javaCodeConventionsGuideline.idx
                        )
                )
                .from(javaCodeConventionsGuideline)
                .where(javaCodeConventionsGuideline.javaCodeConventionsIdx.eq(javaCodeConventionsIdx));

        return query.fetch();
    }

}