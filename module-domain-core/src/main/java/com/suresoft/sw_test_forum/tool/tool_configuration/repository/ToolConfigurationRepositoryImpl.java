package com.suresoft.sw_test_forum.tool.tool_configuration.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfiguration;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QDevelopmentEnvironmentInformation.developmentEnvironmentInformation;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.common.domain.QIdeInformation.ideInformation;
import static com.suresoft.sw_test_forum.common.domain.QTargetEnvironmentInformation.targetEnvironmentInformation;
import static com.suresoft.sw_test_forum.common.domain.QTargetInformation.targetInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.tool.tool_configuration.domain.QToolConfiguration.toolConfiguration;

@Repository
@Transactional
public class ToolConfigurationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolConfigurationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolConfiguration.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolConfigurationDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ToolConfigurationDto.class,
                                toolConfiguration.idx,
                                toolConfiguration.createdDate,
                                toolConfiguration.createdByIdx,
                                toolConfiguration.activeStatus,
                                toolConfiguration.views,

                                toolConfiguration.title,
                                toolConfiguration.priority,
                                hashTags.content.as("hashTags"),
                                toolConfiguration.targetToolName
                        )
                )
                .from(toolConfiguration)
                .join(hashTags).on(toolConfiguration.hashTagsIdx.eq(hashTags.idx))
                .where(toolConfiguration.priority.loe(5))
                .orderBy(toolConfiguration.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<ToolConfigurationDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                ToolConfigurationDto.class,
                                toolConfiguration.priority
                        )
                )
                .from(toolConfiguration)
                .where(toolConfiguration.priority.loe(5))
                .orderBy(toolConfiguration.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolConfigurationSearchDto
     * @return
     */
    public PageImpl<ToolConfigurationDto> findAll(Pageable pageable, ToolConfigurationSearchDto toolConfigurationSearchDto) {
        JPQLQuery<ToolConfigurationDto> query = queryFactory.select(
                        Projections.bean(
                                ToolConfigurationDto.class,
                                toolConfiguration.idx,
                                toolConfiguration.createdDate,
                                toolConfiguration.createdByIdx,
                                toolConfiguration.activeStatus,
                                toolConfiguration.views,

                                toolConfiguration.title,
                                toolConfiguration.priority,
                                hashTags.content.as("hashTags"),
                                toolConfiguration.targetToolName
                        )
                )
                .from(toolConfiguration)
                .join(hashTags).on(toolConfiguration.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(toolConfigurationSearchDto)
                        .and(searchCondition2(toolConfigurationSearchDto)))
                .orderBy(toolConfiguration.idx.desc());

        long totalCount = query.fetchCount();
        List<ToolConfigurationDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolConfigurationSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ToolConfigurationSearchDto toolConfigurationSearchDto) {
        BooleanExpression result;

        switch (toolConfigurationSearchDto.getSearchType()) {
            case "TITLE":
                result = toolConfiguration.title.contains(toolConfigurationSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(toolConfigurationSearchDto.getSearchKeyword());
                break;
            case "TARGET_TOOL_NAME":
                result = toolConfiguration.targetToolName.eq(TargetToolName.valueOf(toolConfigurationSearchDto.getSearchKeyword()));
                break;
            case "CONTENT":
                result = toolConfiguration.content.contains(toolConfigurationSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = toolConfiguration.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(toolConfigurationSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(toolConfigurationSearchDto.getSearchKeyword())) {
            result = result.and(toolConfiguration.priority.goe(6));
        }

        return result;
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolConfigurationSearchDto
     * @return
     */
    private BooleanExpression searchCondition2(ToolConfigurationSearchDto toolConfigurationSearchDto) {
        BooleanExpression result = null;

        if ("TARGET_TOOL_NAME".equals(toolConfigurationSearchDto.getSearchType())) {
            switch (toolConfigurationSearchDto.getSearchType2()) {
                case "TITLE":
                    result = toolConfiguration.title.contains(toolConfigurationSearchDto.getSearchKeyword2());
                    break;
                case "HASH_TAGS":
                    result = hashTags.content.contains(toolConfigurationSearchDto.getSearchKeyword2());
                    break;
                case "CONTENT":
                    result = toolConfiguration.content.contains(toolConfigurationSearchDto.getSearchKeyword2());
                    break;
                case "CREATED_BY":
                    result = toolConfiguration.createdByIdx.in(JPAExpressions
                            .select(user.idx)
                            .from(user)
                            .where(user.name.contains(toolConfigurationSearchDto.getSearchKeyword2())));
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
    public ToolConfigurationDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolConfigurationDto.class,
                                toolConfiguration.idx,
                                toolConfiguration.createdDate,
                                toolConfiguration.createdByIdx,
                                toolConfiguration.lastModifiedDate,
                                toolConfiguration.lastModifiedByIdx,
                                toolConfiguration.activeStatus,
                                toolConfiguration.views,

                                toolConfiguration.title,
                                hashTags.content.as("hashTags"),
                                toolConfiguration.priority,
                                toolConfiguration.hashTagsIdx,
                                toolConfiguration.targetToolName,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                toolConfiguration.toolInformationIdx,
                                developmentEnvironmentInformation.developmentEnvironmentName.as("developmentEnvironmentName"),
                                toolConfiguration.developmentEnvironmentInformationIdx,
                                toolConfiguration.programmingLanguage,
                                ideInformation.ideName.as("ideName"),
                                toolConfiguration.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                toolConfiguration.compilerInformationIdx,
                                toolConfiguration.operationEnvironment,
                                targetEnvironmentInformation.targetEnvironmentName.as("targetEnvironmentName"),
                                toolConfiguration.targetEnvironmentInformationIdx,
                                targetInformation.boardName.as("boardName"),
                                targetInformation.architecture.as("architecture"),
                                targetInformation.interfaceMethod.as("interfaceMethod"),
                                targetInformation.debuggerName.as("debuggerName"),
                                targetInformation.executableSize.as("executableSize"),
                                targetInformation.bit.as("bit"),
                                targetInformation.ramUsage.as("ramUsage"),
                                targetInformation.ramFreeSize.as("ramFreeSize"),
                                targetInformation.flashFreeSize.as("flashFreeSize"),
                                toolConfiguration.targetInformationIdx,
                                toolConfiguration.content
                        )
                )
                .from(toolConfiguration)
                .join(hashTags).on(toolConfiguration.hashTagsIdx.eq(hashTags.idx))
                .join(toolInformation).on(toolConfiguration.toolInformationIdx.eq(toolInformation.idx))
                .join(developmentEnvironmentInformation).on(toolConfiguration.developmentEnvironmentInformationIdx.eq(developmentEnvironmentInformation.idx))
                .join(ideInformation).on(toolConfiguration.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(toolConfiguration.compilerInformationIdx.eq(compilerInformation.idx))
                .join(targetEnvironmentInformation).on(toolConfiguration.targetEnvironmentInformationIdx.eq(targetEnvironmentInformation.idx))
                .join(targetInformation).on(toolConfiguration.targetInformationIdx.eq(targetInformation.idx))
                .where(toolConfiguration.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        toolConfiguration.title
                )
                .distinct().from(toolConfiguration)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public ToolConfiguration findConfigurationPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolConfiguration.class,
                                toolConfiguration.priority
                        )
                )
                .from(toolConfiguration)
                .where(toolConfiguration.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(toolConfiguration)
                .set(toolConfiguration.views, toolConfiguration.views.add(1))
                .where(toolConfiguration.idx.eq(idx))
                .execute();
    }
}