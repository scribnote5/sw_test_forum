package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.QCweGuideline.cweGuideline;
import static com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.QCweGuidelineLike.cweGuidelineLike;
import static com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.QStyleCopGuideline.styleCopGuideline;
import static com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.QStyleCopGuidelineLike.styleCopGuidelineLike;

@Repository
@Transactional
public class StyleCopGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<StyleCopGuidelineDto> findTop10ByViews() {
        JPQLQuery<StyleCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineDto.class,
                                styleCopGuideline.idx,
                                styleCopGuideline.createdDate,
                                styleCopGuideline.createdByIdx,
                                styleCopGuideline.activeStatus,
                                styleCopGuideline.views,

                                styleCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                ExpressionUtils.as(JPAExpressions
                                        .select(styleCopGuidelineLike.idx.count())
                                        .from(styleCopGuidelineLike)
                                        .where(styleCopGuidelineLike.styleCopGuidelineIdx.eq(styleCopGuideline.idx)), "likeCountInList")
                        )
                )
                .from(styleCopGuideline)
                .join(hashTags).on(styleCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .orderBy(styleCopGuideline.views.desc())
                .limit(10);

        return query.fetch();
    }


    /**
     * 리스트 조회
     *
     * @param pageable
     * @param styleCopGuidelineSearchDto
     * @return
     */
    public PageImpl<StyleCopGuidelineDto> findAll(Pageable pageable, StyleCopGuidelineSearchDto styleCopGuidelineSearchDto) {
        JPQLQuery<StyleCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineDto.class,
                                styleCopGuideline.idx,
                                styleCopGuideline.createdDate,
                                styleCopGuideline.createdByIdx,
                                styleCopGuideline.activeStatus,
                                styleCopGuideline.views,

                                styleCopGuideline.styleCopIdx,
                                styleCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                styleCopGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(styleCopGuidelineLike.idx.count())
                                        .from(styleCopGuidelineLike)
                                        .where(styleCopGuidelineLike.styleCopGuidelineIdx.eq(styleCopGuideline.idx)), "likeCountInList")
                        )
                )
                .from(styleCopGuideline)
                .join(hashTags).on(styleCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(styleCopGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(styleCopGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(styleCopGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(styleCopGuidelineSearchDto),
                        searchCondition(styleCopGuidelineSearchDto.getStyleCopIdx()))
                .orderBy(styleCopGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<StyleCopGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param styleCopGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(StyleCopGuidelineSearchDto styleCopGuidelineSearchDto) {
        BooleanExpression result;

        switch (styleCopGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = styleCopGuideline.title.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = styleCopGuideline.guidelineResult.eq(GuidelineResult.valueOf(styleCopGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = styleCopGuideline.content.contains(styleCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = styleCopGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(styleCopGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * StyleCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    public List<StyleCopGuidelineDto> findAll(long styleCopIdx) {
        JPQLQuery<StyleCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineDto.class,
                                styleCopGuideline.idx,
                                styleCopGuideline.createdDate,
                                styleCopGuideline.createdByIdx,
                                styleCopGuideline.activeStatus,
                                styleCopGuideline.views,

                                styleCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                styleCopGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(styleCopGuidelineLike.idx.count())
                                        .from(styleCopGuidelineLike)
                                        .where(styleCopGuidelineLike.styleCopGuidelineIdx.eq(styleCopGuideline.idx)), "likeCountInList")
                        )
                )
                .from(styleCopGuideline)
                .join(hashTags).on(styleCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(styleCopGuideline.styleCopIdx.eq(styleCopIdx))
                .orderBy(styleCopGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * StyleCop 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param styleCopIdx
     * @return
     */
    private BooleanExpression searchCondition(long styleCopIdx) {
        BooleanExpression result;

        if (styleCopIdx == 0) {
            result = null;
        } else {
            result = styleCopGuideline.styleCopIdx.eq(styleCopIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StyleCopGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineDto.class,
                                styleCopGuideline.idx,
                                styleCopGuideline.createdDate,
                                styleCopGuideline.createdByIdx,
                                styleCopGuideline.lastModifiedDate,
                                styleCopGuideline.lastModifiedByIdx,
                                styleCopGuideline.activeStatus,
                                styleCopGuideline.views,

                                styleCopGuideline.styleCopIdx,
                                styleCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                styleCopGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                styleCopGuideline.projectInformationIdx,
                                styleCopGuideline.guidelineResult,
                                styleCopGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                styleCopGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                styleCopGuideline.compilerInformationIdx,
                                styleCopGuideline.content,
                                styleCopGuideline.nonCompliantExample,
                                styleCopGuideline.compliantExample,
                                styleCopGuideline.badCasePositionList,
                                styleCopGuideline.goodCasePositionList
                        )
                )
                .from(styleCopGuideline)
                .join(hashTags).on(styleCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(styleCopGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(styleCopGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(styleCopGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(styleCopGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        styleCopGuideline.title
                )
                .distinct().from(styleCopGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(styleCopGuideline)
                .set(styleCopGuideline.views, styleCopGuideline.views.add(1))
                .where(styleCopGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * StyleCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    public List<StyleCopGuidelineDto> findAllWhenDelete(long styleCopIdx) {
        JPQLQuery<StyleCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineDto.class,
                                styleCopGuideline.idx
                        )
                )
                .from(styleCopGuideline)
                .where(styleCopGuideline.styleCopIdx.eq(styleCopIdx));

        return query.fetch();
    }
}