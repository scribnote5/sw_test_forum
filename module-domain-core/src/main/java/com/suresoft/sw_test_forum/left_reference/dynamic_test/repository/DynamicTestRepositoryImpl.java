package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTest;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestSearchDto;
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

import static com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.QDynamicTest.dynamicTest;
import static com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.QDynamicTestLike.dynamicTestLike;

@Repository
@Transactional
public class DynamicTestRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DynamicTestRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DynamicTest.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<DynamicTestDto> findTop10ByViews() {
        JPQLQuery<DynamicTestDto> query = queryFactory.select(
                        Projections.bean(
                                DynamicTestDto.class,
                                dynamicTest.idx,
                                dynamicTest.createdDate,
                                dynamicTest.createdByIdx,
                                dynamicTest.activeStatus,
                                dynamicTest.views,

                                dynamicTest.title,
                                hashTags.content.as("hashTags"),
                                ExpressionUtils.as(JPAExpressions
                                        .select(dynamicTestLike.idx.count())
                                        .from(dynamicTestLike)
                                        .where(dynamicTestLike.dynamicTestIdx.eq(dynamicTest.idx)), "likeCountInList")
                        )
                )
                .from(dynamicTest)
                .join(hashTags).on(dynamicTest.hashTagsIdx.eq(hashTags.idx))
                .orderBy(dynamicTest.views.desc())
                .limit(10);

        return query.fetch();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param dynamicTestSearchDto
     * @return
     */
    public PageImpl<DynamicTestDto> findAll(Pageable pageable, DynamicTestSearchDto dynamicTestSearchDto) {
        JPQLQuery<DynamicTestDto> query = queryFactory.select(
                        Projections.bean(
                                DynamicTestDto.class,
                                dynamicTest.idx,
                                dynamicTest.createdDate,
                                dynamicTest.createdByIdx,
                                dynamicTest.activeStatus,
                                dynamicTest.views,

                                dynamicTest.title,
                                hashTags.content.as("hashTags"),
                                dynamicTest.guidelineResult,
                                ExpressionUtils.as(JPAExpressions
                                        .select(dynamicTestLike.idx.count())
                                        .from(dynamicTestLike)
                                        .where(dynamicTestLike.dynamicTestIdx.eq(dynamicTest.idx)), "likeCountInList")
                        )
                )
                .from(dynamicTest)
                .join(hashTags).on(dynamicTest.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(dynamicTest.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(dynamicTest.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(dynamicTest.compilerInformationIdx.eq(compilerInformation.idx))

                .where(searchCondition(dynamicTestSearchDto)
                        .and(searchCondition2(dynamicTestSearchDto)))
                .orderBy(dynamicTest.idx.desc());

        long totalCount = query.fetchCount();
        List<DynamicTestDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param dynamicTestSearchDto
     * @return
     */
    private BooleanExpression searchCondition(DynamicTestSearchDto dynamicTestSearchDto) {
        BooleanExpression result;

        switch (dynamicTestSearchDto.getSearchType()) {
            case "TITLE":
                result = dynamicTest.title.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "PROJECT_INFORMATION":
                result = projectInformation.projectName.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "GUIDELINE_RESULT":
                result = dynamicTest.guidelineResult.eq(GuidelineResult.valueOf(dynamicTestSearchDto.getSearchKeyword()));
                break;
            case "TOOL_INFORMATION":
                result = toolInformation.toolName.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "COMPILER_INFORMATION":
                result = compilerInformation.compilerName.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = dynamicTest.content.contains(dynamicTestSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = dynamicTest.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(dynamicTestSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 리스트 조회 조건
     *
     * @param dynamicTestSearchDto
     * @return
     */
    private BooleanExpression searchCondition2(DynamicTestSearchDto dynamicTestSearchDto) {
        BooleanExpression result = null;

        if ("GUIDELINE_RESULT".equals(dynamicTestSearchDto.getSearchType())) {
            switch (dynamicTestSearchDto.getSearchType2()) {
                case "TITLE":
                    result = dynamicTest.title.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "HASH_TAGS":
                    result = hashTags.content.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "PROJECT_INFORMATION":
                    result = projectInformation.projectName.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "TOOL_INFORMATION":
                    result = toolInformation.toolName.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "COMPILER_INFORMATION":
                    result = compilerInformation.compilerName.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "CONTENT":
                    result = dynamicTest.content.contains(dynamicTestSearchDto.getSearchKeyword2());
                    break;
                case "CREATED_BY":
                    result = dynamicTest.createdByIdx.in(JPAExpressions
                            .select(user.idx)
                            .from(user)
                            .where(user.name.contains(dynamicTestSearchDto.getSearchKeyword2())));
                    break;
                default:
                    result = null;
            }
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public DynamicTestDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                DynamicTestDto.class,
                                dynamicTest.idx,
                                dynamicTest.createdDate,
                                dynamicTest.createdByIdx,
                                dynamicTest.lastModifiedDate,
                                dynamicTest.lastModifiedByIdx,
                                dynamicTest.activeStatus,
                                dynamicTest.views,

                                dynamicTest.title,
                                hashTags.content.as("hashTags"),
                                dynamicTest.hashTagsIdx,
                                projectInformation.projectName.as("projectName"),
                                dynamicTest.projectInformationIdx,
                                dynamicTest.guidelineResult,
                                dynamicTest.guidelineResultNote,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                dynamicTest.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                dynamicTest.compilerInformationIdx,
                                dynamicTest.content,
                                dynamicTest.nonCompliantExample,
                                dynamicTest.compliantExample,
                                dynamicTest.badCasePositionList,
                                dynamicTest.goodCasePositionList
                        )
                )
                .from(dynamicTest)
                .join(hashTags).on(dynamicTest.hashTagsIdx.eq(hashTags.idx))
                .join(projectInformation).on(dynamicTest.projectInformationIdx.eq(projectInformation.idx))
                .join(toolInformation).on(dynamicTest.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(dynamicTest.compilerInformationIdx.eq(compilerInformation.idx))
                .where(dynamicTest.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        dynamicTest.title
                )
                .distinct().from(dynamicTest)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(dynamicTest)
                .set(dynamicTest.views, dynamicTest.views.add(1))
                .where(dynamicTest.idx.eq(idx))
                .execute();
    }
}