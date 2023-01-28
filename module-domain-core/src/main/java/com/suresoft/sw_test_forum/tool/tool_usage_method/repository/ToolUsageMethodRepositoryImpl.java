package com.suresoft.sw_test_forum.tool.tool_usage_method.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethod;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.tool.tool_usage_method.domain.QToolUsageMethod.toolUsageMethod;

@Repository
@Transactional
public class ToolUsageMethodRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolUsageMethodRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolUsageMethod.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolUsageMethodDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ToolUsageMethodDto.class,
                                toolUsageMethod.idx,
                                toolUsageMethod.createdDate,
                                toolUsageMethod.createdByIdx,
                                toolUsageMethod.activeStatus,
                                toolUsageMethod.views,

                                toolUsageMethod.title,
                                toolUsageMethod.priority,
                                hashTags.content.as("hashTags"),
                                toolUsageMethod.toolCategory,
                                toolUsageMethod.targetToolName
                        )
                )
                .from(toolUsageMethod)
                .join(hashTags).on(toolUsageMethod.hashTagsIdx.eq(hashTags.idx))
                .where(toolUsageMethod.priority.loe(5))
                .orderBy(toolUsageMethod.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<ToolUsageMethodDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                ToolUsageMethodDto.class,
                                toolUsageMethod.priority
                        )
                )
                .from(toolUsageMethod)
                .where(toolUsageMethod.priority.loe(5))
                .orderBy(toolUsageMethod.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolUsageMethodSearchDto
     * @return
     */
    public PageImpl<ToolUsageMethodDto> findAll(Pageable pageable, ToolUsageMethodSearchDto toolUsageMethodSearchDto) {
        JPQLQuery<ToolUsageMethodDto> query = queryFactory.select(
                        Projections.bean(
                                ToolUsageMethodDto.class,
                                toolUsageMethod.idx,
                                toolUsageMethod.createdDate,
                                toolUsageMethod.createdByIdx,
                                toolUsageMethod.activeStatus,
                                toolUsageMethod.views,

                                toolUsageMethod.title,
                                toolUsageMethod.priority,
                                hashTags.content.as("hashTags"),
                                toolUsageMethod.toolCategory,
                                toolUsageMethod.targetToolName
                        )
                )
                .from(toolUsageMethod)
                .join(hashTags).on(toolUsageMethod.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(toolUsageMethodSearchDto)
                        .and(searchCondition2(toolUsageMethodSearchDto)))
                .orderBy(toolUsageMethod.idx.desc());

        long totalCount = query.fetchCount();
        List<ToolUsageMethodDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolUsageMethodSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ToolUsageMethodSearchDto toolUsageMethodSearchDto) {
        BooleanExpression result;

        switch (toolUsageMethodSearchDto.getSearchType()) {
            case "TITLE":
                result = toolUsageMethod.title.contains(toolUsageMethodSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(toolUsageMethodSearchDto.getSearchKeyword());
                break;
            case "TARGET_TOOL_NAME":
                result = toolUsageMethod.targetToolName.eq(TargetToolName.valueOf(toolUsageMethodSearchDto.getSearchKeyword()));
                break;
            case "TOOL_CATEGORY":
                result = toolUsageMethod.toolCategory.eq(ToolCategory.valueOf(toolUsageMethodSearchDto.getSearchKeyword()));
                break;
            case "CONTENT":
                result = toolUsageMethod.content.contains(toolUsageMethodSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = toolUsageMethod.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(toolUsageMethodSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(toolUsageMethodSearchDto.getSearchKeyword())) {
            result = result.and(toolUsageMethod.priority.goe(6));
        }

        return result;
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolUsageMethodSearchDto
     * @return
     */
    private BooleanExpression searchCondition2(ToolUsageMethodSearchDto toolUsageMethodSearchDto) {
        BooleanExpression result = null;

        if ("TARGET_TOOL_NAME".equals(toolUsageMethodSearchDto.getSearchType()) || "TOOL_CATEGORY".equals(toolUsageMethodSearchDto.getSearchType())) {
            switch (toolUsageMethodSearchDto.getSearchType2()) {
                case "TITLE":
                    result = toolUsageMethod.title.contains(toolUsageMethodSearchDto.getSearchKeyword2());
                    break;
                case "HASH_TAGS":
                    result = hashTags.content.contains(toolUsageMethodSearchDto.getSearchKeyword2());
                    break;
                case "CONTENT":
                    result = toolUsageMethod.content.contains(toolUsageMethodSearchDto.getSearchKeyword2());
                    break;
                case "CREATED_BY":
                    result = toolUsageMethod.createdByIdx.in(JPAExpressions
                            .select(user.idx)
                            .from(user)
                            .where(user.name.contains(toolUsageMethodSearchDto.getSearchKeyword2())));
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
    public ToolUsageMethodDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolUsageMethodDto.class,
                                toolUsageMethod.idx,
                                toolUsageMethod.createdDate,
                                toolUsageMethod.createdByIdx,
                                toolUsageMethod.lastModifiedDate,
                                toolUsageMethod.lastModifiedByIdx,
                                toolUsageMethod.activeStatus,
                                toolUsageMethod.views,

                                toolUsageMethod.title,
                                hashTags.content.as("hashTags"),
                                toolUsageMethod.priority,
                                toolUsageMethod.hashTagsIdx,
                                toolUsageMethod.toolCategory,
                                toolUsageMethod.targetToolName,
                                toolUsageMethod.content
                        )
                )
                .from(toolUsageMethod)
                .join(hashTags).on(toolUsageMethod.hashTagsIdx.eq(hashTags.idx))
                .where(toolUsageMethod.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        toolUsageMethod.title
                )
                .distinct().from(toolUsageMethod)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public ToolUsageMethod findToolUsageMethodPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolUsageMethod.class,
                                toolUsageMethod.priority
                        )
                )
                .from(toolUsageMethod)
                .where(toolUsageMethod.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(toolUsageMethod)
                .set(toolUsageMethod.views, toolUsageMethod.views.add(1))
                .where(toolUsageMethod.idx.eq(idx))
                .execute();
    }
}