package com.suresoft.sw_test_forum.metric.metric_guideline.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineSearchDto;
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
import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuideline.metricGuideline;
import static com.suresoft.sw_test_forum.metric.metric_guideline.domain.QMetricGuidelineLike.metricGuidelineLike;

@Repository
@Transactional
public class MetricGuidelineRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MetricGuidelineRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MetricGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param metricGuidelineSearchDto
     * @return
     */
    public PageImpl<MetricGuidelineDto> findAll(Pageable pageable, MetricGuidelineSearchDto metricGuidelineSearchDto) {
        JPQLQuery<MetricGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MetricGuidelineDto.class,
                                metricGuideline.idx,
                                metricGuideline.createdDate,
                                metricGuideline.createdByIdx,
                                metricGuideline.activeStatus,
                                metricGuideline.views,

                                metricGuideline.metricIdx,
                                metricGuideline.title,
                                hashTags.content.as("hashTags"),
                                metricGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(metricGuidelineLike.idx.count())
                                        .from(metricGuidelineLike)
                                        .where(metricGuidelineLike.metricGuidelineIdx.eq(metricGuideline.idx)), "likeCountInList")
                        )
                )
                .from(metricGuideline)
                .join(hashTags).on(metricGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(metricGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(metricGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(metricGuideline.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(metricGuidelineSearchDto),
                        searchCondition(metricGuidelineSearchDto.getMetricIdx()))
                .orderBy(metricGuideline.idx.desc());

        long totalCount = query.fetchCount();
        List<MetricGuidelineDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param metricGuidelineSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MetricGuidelineSearchDto metricGuidelineSearchDto) {
        BooleanExpression result;

        switch (metricGuidelineSearchDto.getSearchType()) {
            case "TITLE":
                result = metricGuideline.title.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = metricGuideline.guidelineResult.eq(GuidelineResult.valueOf(metricGuidelineSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = metricGuideline.content.contains(metricGuidelineSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = metricGuideline.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(metricGuidelineSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    public List<MetricGuidelineDto> findAll(long metricIdx) {
        JPQLQuery<MetricGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MetricGuidelineDto.class,
                                metricGuideline.idx,
                                metricGuideline.createdDate,
                                metricGuideline.createdByIdx,
                                metricGuideline.activeStatus,
                                metricGuideline.views,

                                metricGuideline.title,
                                hashTags.content.as("hashTags"),
                                metricGuideline.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(metricGuidelineLike.idx.count())
                                        .from(metricGuidelineLike)
                                        .where(metricGuidelineLike.metricGuidelineIdx.eq(metricGuideline.idx)), "likeCountInList")
                        )
                )
                .from(metricGuideline)
                .join(hashTags).on(metricGuideline.hashTagsIdx.eq(hashTags.idx))
                .where(metricGuideline.metricIdx.eq(metricIdx))
                .orderBy(metricGuideline.idx.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param metricIdx
     * @return
     */
    private BooleanExpression searchCondition(long metricIdx) {
        BooleanExpression result;

        if (metricIdx == 0) {
            result = null;
        } else {
            result = metricGuideline.metricIdx.eq(metricIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricGuidelineDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MetricGuidelineDto.class,
                                metricGuideline.idx,
                                metricGuideline.createdDate,
                                metricGuideline.createdByIdx,
                                metricGuideline.lastModifiedDate,
                                metricGuideline.lastModifiedByIdx,
                                metricGuideline.activeStatus,
                                metricGuideline.views,

                                metricGuideline.metricIdx,
                                metricGuideline.title,
                                hashTags.content.as("hashTags"),
                                metricGuideline.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                metricGuideline.projectInformationIdx,
                                metricGuideline.guidelineResult,
                                metricGuideline.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                metricGuideline.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                metricGuideline.compilerInformationIdx,
                                metricGuideline.content,
                                metricGuideline.nonCompliantExample,
                                metricGuideline.compliantExample,
                                metricGuideline.badCasePositionList,
                                metricGuideline.goodCasePositionList
                        )
                )
                .from(metricGuideline)
                .join(hashTags).on(metricGuideline.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(metricGuideline.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(metricGuideline.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(metricGuideline.compilerInformationIdx.eq(compilerInformation.idx))
                .where(metricGuideline.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        metricGuideline.title
                )
                .distinct().from(metricGuideline)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(metricGuideline)
                .set(metricGuideline.views, metricGuideline.views.add(1))
                .where(metricGuideline.idx.eq(idx))
                .execute();
    }

    /**
     * 메트릭 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param metricIdx
     * @return
     */
    public List<MetricGuidelineDto> findAllWhenDelete(long metricIdx) {
        JPQLQuery<MetricGuidelineDto> query = queryFactory.select(
                        Projections.bean(
                                MetricGuidelineDto.class,
                                metricGuideline.idx
                        )
                )
                .from(metricGuideline)
                .where(metricGuideline.metricIdx.eq(metricIdx));

        return query.fetch();
    }
}