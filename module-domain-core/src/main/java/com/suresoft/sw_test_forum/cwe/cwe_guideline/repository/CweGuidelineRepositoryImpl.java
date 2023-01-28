package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuideline.misraCppGuideline;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuidelineLike.misraCppGuidelineLike;

@Repository
@Transactional
public class CweGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<CweGuidelineDto> findTop10ByViews() {
        JPQLQuery<CweGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweGuidelineDto.class,
                                cweGuideline.idx,
                                cweGuideline.createdDate,
                                cweGuideline.createdByIdx,
                                cweGuideline.activeStatus,
                                cweGuideline.views,

                                cweGuideline.title,
                                hashTags.content.as("hashTags"),
                                ExpressionUtils.as(JPAExpressions
                                        .select(cweGuidelineLike.idx.count())
                                        .from(cweGuidelineLike)
                                        .where(cweGuidelineLike.cweGuidelineIdx.eq(cweGuideline.idx)), "likeCountInList")
                        )
                )
                .from(cweGuideline)
                .join(hashTags).on(cweGuideline.hashTagsIdx.eq(hashTags.idx))
                .orderBy(cweGuideline.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweGuidelineSearchDto
     * @return
     */
    public PageImpl<CweGuidelineDto> findAll(Pageable pageable, CweGuidelineSearchDto cweGuidelineSearchDto) {
        JPQLQuery<CweGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweGuidelineDto.class,
                                cweGuideline.idx,
                                cweGuideline.createdDate,
                                cweGuideline.createdByIdx,
                                cweGuideline.activeStatus,
                                cweGuideline.views,

                                cweGuideline.cweIdx,
                                cweGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(cweGuidelineLike.idx.count())
                                        .from(cweGuidelineLike)
                                        .where(cweGuidelineLike.cweGuidelineIdx.eq(cweGuideline.idx)), "likeCountInList")
                        )
                )
                .from(cweGuideline)
                .join(hashTags).on(cweGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(cweGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(cweGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(cweGuidelineSearchDto),
                        searchCondition(cweGuidelineSearchDto.getCweIdx()))
                .orderBy(cweGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<CweGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweGuidelineSearchDto cweGuidelineSearchDto) {
        BooleanExpression result;

        switch (cweGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = cweGuideline.title.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = cweGuideline.guidelineResult.eq(GuidelineResult.valueOf(cweGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cweGuideline.content.contains(cweGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cweGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweIdx
     * @return
     */
    public List<CweGuidelineDto> findAll(long cweIdx) {
        JPQLQuery<CweGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweGuidelineDto.class,
                                cweGuideline.idx,
                                cweGuideline.createdDate,
                                cweGuideline.createdByIdx,
                                cweGuideline.activeStatus,
                                cweGuideline.views,

                                cweGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(cweGuidelineLike.idx.count())
                                        .from(cweGuidelineLike)
                                        .where(cweGuidelineLike.cweGuidelineIdx.eq(cweGuideline.idx)), "likeCountInList")
                        )
                )
                .from(cweGuideline)
                .join(hashTags).on(cweGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(cweGuideline.cweIdx.eq(cweIdx))
                .orderBy(cweGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param cweIdx
     * @return
     */
    private BooleanExpression searchCondition(long cweIdx) {
        BooleanExpression result;

        if (cweIdx == 0) {
            result = null;
        } else {
            result = cweGuideline.cweIdx.eq(cweIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweGuidelineDto.class,
                                cweGuideline.idx,
                                cweGuideline.createdDate,
                                cweGuideline.createdByIdx,
                                cweGuideline.lastModifiedDate,
                                cweGuideline.lastModifiedByIdx,
                                cweGuideline.activeStatus,
                                cweGuideline.views,

                                cweGuideline.cweIdx,
                                cweGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                cweGuideline.projectInformationIdx,
                                cweGuideline.guidelineResult,
                                cweGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                cweGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweGuideline.compilerInformationIdx,
                                cweGuideline.content,
                                cweGuideline.nonCompliantExample,
                                cweGuideline.compliantExample,
                                cweGuideline.badCasePositionList,
                                cweGuideline.goodCasePositionList
                        )
                )
                .from(cweGuideline)
                .join(hashTags).on(cweGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(cweGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(cweGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        cweGuideline.title
                )
                .distinct().from(cweGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cweGuideline)
                .set(cweGuideline.views, cweGuideline.views.add(1))
                .where(cweGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweIdx
     * @return
     */
    public List<CweGuidelineDto> findAllWhenDelete(long cweIdx) {
        JPQLQuery<CweGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweGuidelineDto.class,
                                cweGuideline.idx
                        )
                )
                .from(cweGuideline)
                .where(cweGuideline.cweIdx.eq(cweIdx));

        return query.fetch();
    }
}