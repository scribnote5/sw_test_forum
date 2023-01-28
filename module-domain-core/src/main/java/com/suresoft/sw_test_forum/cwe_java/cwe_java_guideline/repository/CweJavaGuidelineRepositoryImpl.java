package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.QCweJavaGuideline.cweJavaGuideline;
import static com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.QCweJavaGuidelineLike.cweJavaGuidelineLike;

@Repository
@Transactional
public class CweJavaGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweJavaGuidelineSearchDto
     * @return
     */
    public PageImpl<CweJavaGuidelineDto> findAll(Pageable pageable, CweJavaGuidelineSearchDto cweJavaGuidelineSearchDto) {
        JPQLQuery<CweJavaGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaGuidelineDto.class,
                                cweJavaGuideline.idx,
                                cweJavaGuideline.createdDate,
                                cweJavaGuideline.createdByIdx,
                                cweJavaGuideline.activeStatus,
                                cweJavaGuideline.views,

                                cweJavaGuideline.cweJavaIdx,
                                cweJavaGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweJavaGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(cweJavaGuidelineLike.idx.count())
                                        .from(cweJavaGuidelineLike)
                                        .where(cweJavaGuidelineLike.cweJavaGuidelineIdx.eq(cweJavaGuidelineLike.idx)), "likeCountInList")
                        )
                )
                .from(cweJavaGuideline)
                .join(hashTags).on(cweJavaGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(cweJavaGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(cweJavaGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweJavaGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(cweJavaGuidelineSearchDto),
                        searchCondition(cweJavaGuidelineSearchDto.getCweJavaIdx()))
                .orderBy(cweJavaGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<CweJavaGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param cweJavaGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CweJavaGuidelineSearchDto cweJavaGuidelineSearchDto) {
        BooleanExpression result;

        switch (cweJavaGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = cweJavaGuideline.title.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = cweJavaGuideline.guidelineResult.eq(GuidelineResult.valueOf(cweJavaGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = cweJavaGuideline.content.contains(cweJavaGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = cweJavaGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(cweJavaGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public List<CweJavaGuidelineDto> findAll(long cweJavaIdx) {
        JPQLQuery<CweJavaGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaGuidelineDto.class,
                                cweJavaGuideline.idx,
                                cweJavaGuideline.createdDate,
                                cweJavaGuideline.createdByIdx,
                                cweJavaGuideline.activeStatus,
                                cweJavaGuideline.views,

                                cweJavaGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweJavaGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(cweJavaGuidelineLike.idx.count())
                                        .from(cweJavaGuidelineLike)
                                        .where(cweJavaGuidelineLike.cweJavaGuidelineIdx.eq(cweJavaGuidelineLike.idx)), "likeCountInList")
                        )
                )
                .from(cweJavaGuideline)
                .join(hashTags).on(cweJavaGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(cweJavaGuideline.cweJavaIdx.eq(cweJavaIdx))
                .orderBy(cweJavaGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param cweJavaIdx
     * @return
     */
    private BooleanExpression searchCondition(long cweJavaIdx) {
        BooleanExpression result;

        if (cweJavaIdx == 0) {
            result = null;
        } else {
            result = cweJavaGuideline.cweJavaIdx.eq(cweJavaIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaGuidelineDto.class,
                                cweJavaGuideline.idx,
                                cweJavaGuideline.createdDate,
                                cweJavaGuideline.createdByIdx,
                                cweJavaGuideline.lastModifiedDate,
                                cweJavaGuideline.lastModifiedByIdx,
                                cweJavaGuideline.activeStatus,
                                cweJavaGuideline.views,

                                cweJavaGuideline.cweJavaIdx,
                                cweJavaGuideline.title,
                                hashTags.content.as("hashTags"),
                                cweJavaGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                cweJavaGuideline.projectInformationIdx,
                                cweJavaGuideline.guidelineResult,
                                cweJavaGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                cweJavaGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                cweJavaGuideline.compilerInformationIdx,
                                cweJavaGuideline.content,
                                cweJavaGuideline.nonCompliantExample,
                                cweJavaGuideline.compliantExample,
                                cweJavaGuideline.badCasePositionList,
                                cweJavaGuideline.goodCasePositionList
                        )
                )
                .from(cweJavaGuideline)
                .join(hashTags).on(cweJavaGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(cweJavaGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(cweJavaGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(cweJavaGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(cweJavaGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        cweJavaGuideline.title
                )
                .distinct().from(cweJavaGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(cweJavaGuideline)
                .set(cweJavaGuideline.views, cweJavaGuideline.views.add(1))
                .where(cweJavaGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public List<CweJavaGuidelineDto> findAllWhenDelete(long cweJavaIdx) {
        JPQLQuery<CweJavaGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                CweJavaGuidelineDto.class,
                                cweJavaGuideline.idx
                        )
                )
                .from(cweJavaGuideline)
                .where(cweJavaGuideline.cweJavaIdx.eq(cweJavaIdx));

        return query.fetch();
    }
}