package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.Toolchain;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainSearchDto;
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
import static com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.QStaticTool.staticTool;
import static com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.QToolchain.toolchain;

@Repository
@Transactional
public class ToolchainRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolchainRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Toolchain.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolchainDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                ToolchainDto.class,
                                toolchain.idx,
                                toolchain.createdDate,
                                toolchain.createdByIdx,
                                toolchain.activeStatus,
                                toolchain.views,

                                toolchain.title,
                                toolchain.priority,
                                hashTags.content.as("hashTags"),
                                toolchain.toolCategory
                        )
                )
                .from(toolchain)
                .join(hashTags).on(toolchain.hashTagsIdx.eq(hashTags.idx))
                .where(toolchain.priority.loe(5))
                .orderBy(toolchain.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<ToolchainDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                ToolchainDto.class,
                                toolchain.priority
                        )
                )
                .from(toolchain)
                .where(toolchain.priority.loe(5))
                .orderBy(toolchain.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolchainSearchDto
     * @return
     */
    public PageImpl<ToolchainDto> findAll(Pageable pageable, ToolchainSearchDto toolchainSearchDto) {
        JPQLQuery<ToolchainDto> query = queryFactory.select(
                        Projections.bean(
                                ToolchainDto.class,
                                toolchain.idx,
                                toolchain.createdDate,
                                toolchain.createdByIdx,
                                toolchain.activeStatus,
                                toolchain.views,

                                toolchain.title,
                                toolchain.priority,
                                hashTags.content.as("hashTags"),
                                toolchain.toolCategory
                        )
                )
                .from(toolchain)
                .join(hashTags).on(toolchain.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(toolchainSearchDto))
                .orderBy(toolchain.idx.desc());

        long totalCount = query.fetchCount();
        List<ToolchainDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param toolchainSearchDto
     * @return
     */
    private BooleanExpression searchCondition(ToolchainSearchDto toolchainSearchDto) {
        BooleanExpression result;

        switch (toolchainSearchDto.getSearchType()) {
            case "TITLE":
                result = toolchain.title.contains(toolchainSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(toolchainSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = toolchain.content.contains(toolchainSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = toolchain.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(toolchainSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(toolchainSearchDto.getSearchKeyword())) {
            result = result.and(toolchain.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolchainDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolchainDto.class,
                                toolchain.idx,
                                toolchain.createdDate,
                                toolchain.createdByIdx,
                                toolchain.lastModifiedDate,
                                toolchain.lastModifiedByIdx,
                                toolchain.activeStatus,
                                toolchain.views,

                                toolchain.title,
                                hashTags.content.as("hashTags"),
                                toolchain.priority,
                                toolchain.hashTagsIdx,
                                toolchain.toolCategory,
                                ideInformation.ideName.as("ideName"),
                                toolchain.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                toolchain.compilerInformationIdx,
                                toolchain.content
                        )
                )
                .from(toolchain)
                .join(hashTags).on(toolchain.hashTagsIdx.eq(hashTags.idx))
                .join(ideInformation).on(toolchain.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(toolchain.compilerInformationIdx.eq(compilerInformation.idx))
                .where(toolchain.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        toolchain.title
                )
                .distinct().from(toolchain)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public Toolchain findToolchainPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Toolchain.class,
                                toolchain.priority
                        )
                )
                .from(toolchain)
                .where(toolchain.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(toolchain)
                .set(toolchain.views, toolchain.views.add(1))
                .where(toolchain.idx.eq(idx))
                .execute();
    }
}