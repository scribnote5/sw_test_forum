package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.QFxCopGuideline.fxCopGuideline;
import static com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.QFxCopGuidelineLike.fxCopGuidelineLike;

@Repository
@Transactional
public class FxCopGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param fxCopGuidelineSearchDto
     * @return
     */
    public PageImpl<FxCopGuidelineDto> findAll(Pageable pageable, FxCopGuidelineSearchDto fxCopGuidelineSearchDto) {
        JPQLQuery<FxCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopGuidelineDto.class,
                                fxCopGuideline.idx,
                                fxCopGuideline.createdDate,
                                fxCopGuideline.createdByIdx,
                                fxCopGuideline.activeStatus,
                                fxCopGuideline.views,

                                fxCopGuideline.fxCopIdx,
                                fxCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                fxCopGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(fxCopGuidelineLike.idx.count())
                                        .from(fxCopGuidelineLike)
                                        .where(fxCopGuidelineLike.fxCopGuidelineIdx.eq(fxCopGuideline.idx)), "likeCountInList")
                        )
                )
                .from(fxCopGuideline)
                .join(hashTags).on(fxCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(fxCopGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(fxCopGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(fxCopGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(fxCopGuidelineSearchDto),
                        searchCondition(fxCopGuidelineSearchDto.getFxCopIdx()))
                .orderBy(fxCopGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<FxCopGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param fxCopGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(FxCopGuidelineSearchDto fxCopGuidelineSearchDto) {
        BooleanExpression result;

        switch (fxCopGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = fxCopGuideline.title.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = fxCopGuideline.guidelineResult.eq(GuidelineResult.valueOf(fxCopGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = fxCopGuideline.content.contains(fxCopGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = fxCopGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(fxCopGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * FxCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    public List<FxCopGuidelineDto> findAll(long fxCopIdx) {
        JPQLQuery<FxCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopGuidelineDto.class,
                                fxCopGuideline.idx,
                                fxCopGuideline.createdDate,
                                fxCopGuideline.createdByIdx,
                                fxCopGuideline.activeStatus,
                                fxCopGuideline.views,

                                fxCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                fxCopGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(fxCopGuidelineLike.idx.count())
                                        .from(fxCopGuidelineLike)
                                        .where(fxCopGuidelineLike.fxCopGuidelineIdx.eq(fxCopGuideline.idx)), "likeCountInList")
                        )
                )
                .from(fxCopGuideline)
                .join(hashTags).on(fxCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(fxCopGuideline.fxCopIdx.eq(fxCopIdx))
                .orderBy(fxCopGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * FxCop 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param fxCopIdx
     * @return
     */
    private BooleanExpression searchCondition(long fxCopIdx) {
        BooleanExpression result;

        if (fxCopIdx == 0) {
            result = null;
        } else {
            result = fxCopGuideline.fxCopIdx.eq(fxCopIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public FxCopGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopGuidelineDto.class,
                                fxCopGuideline.idx,
                                fxCopGuideline.createdDate,
                                fxCopGuideline.createdByIdx,
                                fxCopGuideline.lastModifiedDate,
                                fxCopGuideline.lastModifiedByIdx,
                                fxCopGuideline.activeStatus,
                                fxCopGuideline.views,

                                fxCopGuideline.fxCopIdx,
                                fxCopGuideline.title,
                                hashTags.content.as("hashTags"),
                                fxCopGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                fxCopGuideline.projectInformationIdx,
                                fxCopGuideline.guidelineResult,
                                fxCopGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                fxCopGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                fxCopGuideline.compilerInformationIdx,
                                fxCopGuideline.content,
                                fxCopGuideline.nonCompliantExample,
                                fxCopGuideline.compliantExample,
                                fxCopGuideline.badCasePositionList,
                                fxCopGuideline.goodCasePositionList
                        )
                )
                .from(fxCopGuideline)
                .join(hashTags).on(fxCopGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(fxCopGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(fxCopGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(fxCopGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(fxCopGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        fxCopGuideline.title
                )
                .distinct().from(fxCopGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(fxCopGuideline)
                .set(fxCopGuideline.views, fxCopGuideline.views.add(1))
                .where(fxCopGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * FxCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    public List<FxCopGuidelineDto> findAllWhenDelete(long fxCopIdx) {
        JPQLQuery<FxCopGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                FxCopGuidelineDto.class,
                                fxCopGuideline.idx
                        )
                )
                .from(fxCopGuideline)
                .where(fxCopGuideline.fxCopIdx.eq(fxCopIdx));

        return query.fetch();
    }
}