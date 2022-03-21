package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterTool;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolSearchDto;
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
import static com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.QControllerTesterTool.controllerTesterTool;

@Repository
@Transactional
public class ControllerTesterToolRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ControllerTesterToolRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ControllerTesterTool.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ControllerTesterToolDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ControllerTesterToolDto.class,
                                controllerTesterTool.idx,
                                controllerTesterTool.createdDate,
                                controllerTesterTool.createdByIdx,
                                controllerTesterTool.activeStatus,
                                controllerTesterTool.views,

                                controllerTesterTool.title,
                                controllerTesterTool.priority,
                                hashTags.content.as("hashTags"),
                                controllerTesterTool.toolCategory
                        )
                )
                .from(controllerTesterTool)
                .join(hashTags).on(controllerTesterTool.hashTagsIdx.eq(hashTags.idx))
                .where(controllerTesterTool.priority.loe(5))
                .orderBy(controllerTesterTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<ControllerTesterToolDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                ControllerTesterToolDto.class,
                                controllerTesterTool.priority
                        )
                )
                .from(controllerTesterTool)
                .where(controllerTesterTool.priority.loe(5))
                .orderBy(controllerTesterTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param controllerTesterToolSearchDto
     * @return
     */
    public PageImpl<ControllerTesterToolDto> findAll(Pageable pageable, ControllerTesterToolSearchDto controllerTesterToolSearchDto) {
        JPQLQuery<ControllerTesterToolDto> query = queryFactory.select(
                        Projections.bean(
                                ControllerTesterToolDto.class,
                                controllerTesterTool.idx,
                                controllerTesterTool.createdDate,
                                controllerTesterTool.createdByIdx,
                                controllerTesterTool.activeStatus,
                                controllerTesterTool.views,

                                controllerTesterTool.title,
                                controllerTesterTool.priority,
                                hashTags.content.as("hashTags"),
                                controllerTesterTool.toolCategory
                        )
                )
                .from(controllerTesterTool)
                .join(hashTags).on(controllerTesterTool.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(controllerTesterToolSearchDto))
                .orderBy(controllerTesterTool.idx.desc());

        long totalCount = query.fetchCount();
        List<ControllerTesterToolDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param controllerTesterToolSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ControllerTesterToolSearchDto controllerTesterToolSearchDto) {
        BooleanExpression result;

        switch (controllerTesterToolSearchDto.getSearchType()) {
            case "TITLE":
                result = controllerTesterTool.title.contains(controllerTesterToolSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(controllerTesterToolSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = controllerTesterTool.content.contains(controllerTesterToolSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = controllerTesterTool.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(controllerTesterToolSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(controllerTesterToolSearchDto.getSearchKeyword())) {
            result = result.and(controllerTesterTool.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ControllerTesterToolDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ControllerTesterToolDto.class,
                                controllerTesterTool.idx,
                                controllerTesterTool.createdDate,
                                controllerTesterTool.createdByIdx,
                                controllerTesterTool.lastModifiedDate,
                                controllerTesterTool.lastModifiedByIdx,
                                controllerTesterTool.activeStatus,
                                controllerTesterTool.views,

                                controllerTesterTool.title,
                                hashTags.content.as("hashTags"),
                                controllerTesterTool.priority,
                                controllerTesterTool.hashTagsIdx,
                                controllerTesterTool.toolCategory,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                controllerTesterTool.toolInformationIdx,
                                ideInformation.ideName.as("ideName"),
                                controllerTesterTool.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                controllerTesterTool.compilerInformationIdx,
                                controllerTesterTool.content
                        )
                )
                .from(controllerTesterTool)
                .join(hashTags).on(controllerTesterTool.hashTagsIdx.eq(hashTags.idx))
                .join(toolInformation).on(controllerTesterTool.toolInformationIdx.eq(toolInformation.idx))
                .join(ideInformation).on(controllerTesterTool.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(controllerTesterTool.compilerInformationIdx.eq(compilerInformation.idx))
                .where(controllerTesterTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        controllerTesterTool.title
                )
                .distinct().from(controllerTesterTool)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public ControllerTesterTool findControllerTesterToolPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ControllerTesterTool.class,
                                controllerTesterTool.priority
                        )
                )
                .from(controllerTesterTool)
                .where(controllerTesterTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(controllerTesterTool)
                .set(controllerTesterTool.views, controllerTesterTool.views.add(1))
                .where(controllerTesterTool.idx.eq(idx))
                .execute();
    }
}