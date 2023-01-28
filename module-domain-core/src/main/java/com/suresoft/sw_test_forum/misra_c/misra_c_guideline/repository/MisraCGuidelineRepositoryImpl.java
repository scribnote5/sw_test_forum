package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuideline;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.misra_c.misra_c.domain.QMisraC.misraC;
import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuideline.misraCGuideline;
import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuidelineLike.misraCGuidelineLike;

@Repository
@Transactional
public class MisraCGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCGuidelineDto> findTop10ByViews() {
        JPQLQuery<MisraCGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineDto.class,
                                misraCGuideline.idx,
                                misraCGuideline.createdDate,
                                misraCGuideline.createdByIdx,
                                misraCGuideline.activeStatus,
                                misraCGuideline.views,

                                misraCGuideline.title,
                                hashTags.content.as("hashTags"),
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCGuidelineLike.idx.count())
                                        .from(misraCGuidelineLike)
                                        .where(misraCGuidelineLike.misraCGuidelineIdx.eq(misraCGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCGuideline)
                .join(hashTags).on(misraCGuideline.hashTagsIdx.eq(hashTags.idx))
                .orderBy(misraCGuideline.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCGuidelineSearchDto
     * @return
     */
    public PageImpl<MisraCGuidelineDto> findAll(Pageable pageable, MisraCGuidelineSearchDto misraCGuidelineSearchDto) {
        JPQLQuery<MisraCGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineDto.class,
                                misraCGuideline.idx,
                                misraCGuideline.createdDate,
                                misraCGuideline.createdByIdx,
                                misraCGuideline.activeStatus,
                                misraCGuideline.views,

                                misraCGuideline.misraCIdx,
                                misraCGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCGuidelineLike.idx.count())
                                        .from(misraCGuidelineLike)
                                        .where(misraCGuidelineLike.misraCGuidelineIdx.eq(misraCGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCGuideline)
                .join(hashTags).on(misraCGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(misraCGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(misraCGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(misraCGuidelineSearchDto),
                        searchCondition(misraCGuidelineSearchDto.getMisraCIdx()))
                .orderBy(misraCGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCGuidelineSearchDto misraCGuidelineSearchDto) {
        BooleanExpression result;

        switch (misraCGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = misraCGuideline.title.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = misraCGuideline.guidelineResult.eq(GuidelineResult.valueOf(misraCGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraCGuideline.content.contains(misraCGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraCGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * MISRA C 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    public List<MisraCGuidelineDto> findAll(long misraCIdx) {
        JPQLQuery<MisraCGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineDto.class,
                                misraCGuideline.idx,
                                misraCGuideline.createdDate,
                                misraCGuideline.createdByIdx,
                                misraCGuideline.activeStatus,
                                misraCGuideline.views,

                                misraCGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCGuidelineLike.idx.count())
                                        .from(misraCGuidelineLike)
                                        .where(misraCGuidelineLike.misraCGuidelineIdx.eq(misraCGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCGuideline)
                .join(hashTags).on(misraCGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(misraCGuideline.misraCIdx.eq(misraCIdx))
                .orderBy(misraCGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * MISRA C 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param misraCIdx
     * @return
     */
    private BooleanExpression searchCondition(long misraCIdx) {
        BooleanExpression result;

        if (misraCIdx == 0) {
            result = null;
        } else {
            result = misraCGuideline.misraCIdx.eq(misraCIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineDto.class,
                                misraCGuideline.idx,
                                misraCGuideline.createdDate,
                                misraCGuideline.createdByIdx,
                                misraCGuideline.lastModifiedDate,
                                misraCGuideline.lastModifiedByIdx,
                                misraCGuideline.activeStatus,
                                misraCGuideline.views,

                                misraCGuideline.misraCIdx,
                                misraCGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                misraCGuideline.projectInformationIdx,
                                misraCGuideline.guidelineResult,
                                misraCGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                misraCGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCGuideline.compilerInformationIdx,
                                misraCGuideline.content,
                                misraCGuideline.nonCompliantExample,
                                misraCGuideline.compliantExample,
                                misraCGuideline.badCasePositionList,
                                misraCGuideline.goodCasePositionList
                        )
                )
                .from(misraCGuideline)
                .join(hashTags).on(misraCGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(misraCGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(misraCGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        misraCGuideline.title
                )
                .distinct().from(misraCGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraCGuideline)
                .set(misraCGuideline.views, misraCGuideline.views.add(1))
                .where(misraCGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * MISRA C 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    public List<MisraCGuidelineDto> findAllWhenDelete(long misraCIdx) {
        JPQLQuery<MisraCGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineDto.class,
                                misraCGuideline.idx
                        )
                )
                .from(misraCGuideline)
                .where(misraCGuideline.misraCIdx.eq(misraCIdx));

        return query.fetch();
    }

}