package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShooting;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.common.domain.QIdeInformation.ideInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.QToolTroubleShooting.toolTroubleShooting;

@Repository
@Transactional
public class ToolTroubleShootingRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolTroubleShootingRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolTroubleShooting.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolTroubleShootingDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ToolTroubleShootingDto.class,
                                toolTroubleShooting.idx,
                                toolTroubleShooting.createdDate,
                                toolTroubleShooting.createdByIdx,
                                toolTroubleShooting.activeStatus,
                                toolTroubleShooting.views,

                                toolTroubleShooting.title,
                                toolTroubleShooting.priority,
                                hashTags.content.as("hashTags"),
                                toolTroubleShooting.toolCategory,
                                toolTroubleShooting.targetToolName
                        )
                )
                .from(toolTroubleShooting)
                .join(hashTags).on(toolTroubleShooting.hashTagsIdx.eq(hashTags.idx))
                .where(toolTroubleShooting.priority.loe(5))
                .orderBy(toolTroubleShooting.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<ToolTroubleShootingDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                ToolTroubleShootingDto.class,
                                toolTroubleShooting.priority
                        )
                )
                .from(toolTroubleShooting)
                .where(toolTroubleShooting.priority.loe(5))
                .orderBy(toolTroubleShooting.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolTroubleShootingSearchDto
     * @return
     */
    public PageImpl<ToolTroubleShootingDto> findAll(Pageable pageable, ToolTroubleShootingSearchDto toolTroubleShootingSearchDto) {
        JPQLQuery<ToolTroubleShootingDto> query = queryFactory.select(
                        Projections.bean(
                                ToolTroubleShootingDto.class,
                                toolTroubleShooting.idx,
                                toolTroubleShooting.createdDate,
                                toolTroubleShooting.createdByIdx,
                                toolTroubleShooting.activeStatus,
                                toolTroubleShooting.views,

                                toolTroubleShooting.title,
                                toolTroubleShooting.priority,
                                hashTags.content.as("hashTags"),
                                toolTroubleShooting.toolCategory,
                                toolTroubleShooting.targetToolName
                        )
                )
                .from(toolTroubleShooting)
                .join(hashTags).on(toolTroubleShooting.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(toolTroubleShootingSearchDto)
                        .and(searchCondition2(toolTroubleShootingSearchDto)))
                .orderBy(toolTroubleShooting.idx.desc());

        long totalCount = query.fetchCount();
        List<ToolTroubleShootingDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolTroubleShootingSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ToolTroubleShootingSearchDto toolTroubleShootingSearchDto) {
        BooleanExpression result;

        switch (toolTroubleShootingSearchDto.getSearchType()) {
            case "TITLE":
                result = toolTroubleShooting.title.contains(toolTroubleShootingSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(toolTroubleShootingSearchDto.getSearchKeyword());
                break;
            case "TARGET_TOOL_NAME":
                result = toolTroubleShooting.targetToolName.eq(TargetToolName.valueOf(toolTroubleShootingSearchDto.getSearchKeyword()));
                break;
            case "TOOL_CATEGORY":
                result = toolTroubleShooting.toolCategory.eq(ToolCategory.valueOf(toolTroubleShootingSearchDto.getSearchKeyword()));
                break;
            case "CONTENT":
                result = toolTroubleShooting.content.contains(toolTroubleShootingSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = toolTroubleShooting.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(toolTroubleShootingSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(toolTroubleShootingSearchDto.getSearchKeyword())) {
            result = result.and(toolTroubleShooting.priority.goe(6));
        }

        return result;
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolTroubleShootingSearchDto
     * @return
     */
    private BooleanExpression searchCondition2(ToolTroubleShootingSearchDto toolTroubleShootingSearchDto) {
        BooleanExpression result = null;

        if ("TARGET_TOOL_NAME".equals(toolTroubleShootingSearchDto.getSearchType()) || "TOOL_CATEGORY".equals(toolTroubleShootingSearchDto.getSearchType())) {
            switch (toolTroubleShootingSearchDto.getSearchType2()) {
                case "TITLE":
                    result = toolTroubleShooting.title.contains(toolTroubleShootingSearchDto.getSearchKeyword2());
                    break;
                case "HASH_TAGS":
                    result = hashTags.content.contains(toolTroubleShootingSearchDto.getSearchKeyword2());
                    break;
                case "CONTENT":
                    result = toolTroubleShooting.content.contains(toolTroubleShootingSearchDto.getSearchKeyword2());
                    break;
                case "CREATED_BY":
                    result = toolTroubleShooting.createdByIdx.in(JPAExpressions
                            .select(user.idx)
                            .from(user)
                            .where(user.name.contains(toolTroubleShootingSearchDto.getSearchKeyword2())));
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
    public ToolTroubleShootingDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolTroubleShootingDto.class,
                                toolTroubleShooting.idx,
                                toolTroubleShooting.createdDate,
                                toolTroubleShooting.createdByIdx,
                                toolTroubleShooting.lastModifiedDate,
                                toolTroubleShooting.lastModifiedByIdx,
                                toolTroubleShooting.activeStatus,
                                toolTroubleShooting.views,

                                toolTroubleShooting.title,
                                hashTags.content.as("hashTags"),
                                toolTroubleShooting.priority,
                                toolTroubleShooting.hashTagsIdx,
                                toolTroubleShooting.toolCategory,
                                toolTroubleShooting.targetToolName,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                toolTroubleShooting.toolInformationIdx,
                                ideInformation.ideName.as("ideName"),
                                toolTroubleShooting.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                toolTroubleShooting.compilerInformationIdx,
                                toolTroubleShooting.content
                        )
                )
                .from(toolTroubleShooting)
                .join(hashTags).on(toolTroubleShooting.hashTagsIdx.eq(hashTags.idx))
                .join(toolInformation).on(toolTroubleShooting.toolInformationIdx.eq(toolInformation.idx))
                .join(ideInformation).on(toolTroubleShooting.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(toolTroubleShooting.compilerInformationIdx.eq(compilerInformation.idx))
                .where(toolTroubleShooting.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        toolTroubleShooting.title
                )
                .distinct().from(toolTroubleShooting)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public ToolTroubleShooting findToolTroubleShootingPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolTroubleShooting.class,
                                toolTroubleShooting.priority
                        )
                )
                .from(toolTroubleShooting)
                .where(toolTroubleShooting.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(toolTroubleShooting)
                .set(toolTroubleShooting.views, toolTroubleShooting.views.add(1))
                .where(toolTroubleShooting.idx.eq(idx))
                .execute();
    }
}