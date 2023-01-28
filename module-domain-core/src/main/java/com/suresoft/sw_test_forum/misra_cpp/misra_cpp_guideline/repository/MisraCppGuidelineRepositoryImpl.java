package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuideline.misraCGuideline;
import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuidelineLike.misraCGuidelineLike;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuideline.misraCppGuideline;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuidelineLike.misraCppGuidelineLike;

@Repository
@Transactional
public class MisraCppGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCppGuidelineDto> findTop10ByViews() {
        JPQLQuery<MisraCppGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineDto.class,
                                misraCppGuideline.idx,
                                misraCppGuideline.createdDate,
                                misraCppGuideline.createdByIdx,
                                misraCppGuideline.activeStatus,
                                misraCppGuideline.views,

                                misraCppGuideline.title,
                                hashTags.content.as("hashTags"),
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCppGuidelineLike.idx.count())
                                        .from(misraCppGuidelineLike)
                                        .where(misraCppGuidelineLike.misraCppGuidelineIdx.eq(misraCppGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCppGuideline)
                .join(hashTags).on(misraCppGuideline.hashTagsIdx.eq(hashTags.idx))
                .orderBy(misraCppGuideline.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCppGuidelineSearchDto
     * @return
     */
    public PageImpl<MisraCppGuidelineDto> findAll(Pageable pageable, MisraCppGuidelineSearchDto misraCppGuidelineSearchDto) {
        JPQLQuery<MisraCppGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineDto.class,
                                misraCppGuideline.idx,
                                misraCppGuideline.createdDate,
                                misraCppGuideline.createdByIdx,
                                misraCppGuideline.activeStatus,
                                misraCppGuideline.views,

                                misraCppGuideline.misraCppIdx,
                                misraCppGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCppGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCppGuidelineLike.idx.count())
                                        .from(misraCppGuidelineLike)
                                        .where(misraCppGuidelineLike.misraCppGuidelineIdx.eq(misraCppGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCppGuideline)
                .join(hashTags).on(misraCppGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(misraCppGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(misraCppGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCppGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(misraCppGuidelineSearchDto),
                        searchCondition(misraCppGuidelineSearchDto.getMisraCppIdx()))
                .orderBy(misraCppGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCppGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCppGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCppGuidelineSearchDto misraCppGuidelineSearchDto) {
        BooleanExpression result;

        switch (misraCppGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = misraCppGuideline.title.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = misraCppGuideline.guidelineResult.eq(GuidelineResult.valueOf(misraCppGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraCppGuideline.content.contains(misraCppGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraCppGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCppGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    public List<MisraCppGuidelineDto> findAll(long misraCppIdx) {
        JPQLQuery<MisraCppGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineDto.class,
                                misraCppGuideline.idx,
                                misraCppGuideline.createdDate,
                                misraCppGuideline.createdByIdx,
                                misraCppGuideline.activeStatus,
                                misraCppGuideline.views,

                                misraCppGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCppGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(misraCppGuidelineLike.idx.count())
                                        .from(misraCppGuidelineLike)
                                        .where(misraCppGuidelineLike.misraCppGuidelineIdx.eq(misraCppGuideline.idx)), "likeCountInList")
                        )
                )
                .from(misraCppGuideline)
                .join(hashTags).on(misraCppGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(misraCppGuideline.misraCppIdx.eq(misraCppIdx))
                .orderBy(misraCppGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param misraCppIdx
     * @return
     */
    private BooleanExpression searchCondition(long misraCppIdx) {
        BooleanExpression result;

        if (misraCppIdx == 0) {
            result = null;
        } else {
            result = misraCppGuideline.misraCppIdx.eq(misraCppIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCppGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineDto.class,
                                misraCppGuideline.idx,
                                misraCppGuideline.createdDate,
                                misraCppGuideline.createdByIdx,
                                misraCppGuideline.lastModifiedDate,
                                misraCppGuideline.lastModifiedByIdx,
                                misraCppGuideline.activeStatus,
                                misraCppGuideline.views,

                                misraCppGuideline.misraCppIdx,
                                misraCppGuideline.title,
                                hashTags.content.as("hashTags"),
                                misraCppGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                misraCppGuideline.projectInformationIdx,
                                misraCppGuideline.guidelineResult,
                                misraCppGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                misraCppGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCppGuideline.compilerInformationIdx,
                                misraCppGuideline.content,
                                misraCppGuideline.nonCompliantExample,
                                misraCppGuideline.compliantExample,
                                misraCppGuideline.badCasePositionList,
                                misraCppGuideline.goodCasePositionList
                        )
                )
                .from(misraCppGuideline)
                .join(hashTags).on(misraCppGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(misraCppGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(misraCppGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCppGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCppGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        misraCppGuideline.title
                )
                .distinct().from(misraCppGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraCppGuideline)
                .set(misraCppGuideline.views, misraCppGuideline.views.add(1))
                .where(misraCppGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    public List<MisraCppGuidelineDto> findAllWhenDelete(long misraCppIdx) {
        JPQLQuery<MisraCppGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineDto.class,
                                misraCppGuideline.idx
                        )
                )
                .from(misraCppGuideline)
                .where(misraCppGuideline.misraCppIdx.eq(misraCppIdx));

        return query.fetch();
    }

}