package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticTool;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;
import static com.suresoft.sw_test_forum.common.domain.QIdeInformation.ideInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.QCoverTool.coverTool;
import static com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.QStaticTool.staticTool;
import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;

@Repository
@Transactional
public class StaticToolRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StaticToolRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StaticTool.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StaticToolDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                StaticToolDto.class,
                                staticTool.idx,
                                staticTool.createdDate,
                                staticTool.createdByIdx,
                                staticTool.activeStatus,
                                staticTool.views,

                                staticTool.title,
                                staticTool.priority,
                                hashTags.content.as("hashTags"),
                                staticTool.toolCategory
                        )
                )
                .from(staticTool)
                .join(hashTags).on(staticTool.hashTagsIdx.eq(hashTags.idx))
                .where(staticTool.priority.loe(5))
                .orderBy(staticTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<StaticToolDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                StaticToolDto.class,
                                staticTool.priority
                        )
                )
                .from(staticTool)
                .where(staticTool.priority.loe(5))
                .orderBy(staticTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param staticToolSearchDto
     * @return
     */
    public PageImpl<StaticToolDto> findAll(Pageable pageable, StaticToolSearchDto staticToolSearchDto) {
        JPQLQuery<StaticToolDto> query = queryFactory.select(
                        Projections.bean(
                                StaticToolDto.class,
                                staticTool.idx,
                                staticTool.createdDate,
                                staticTool.createdByIdx,
                                staticTool.activeStatus,
                                staticTool.views,

                                staticTool.title,
                                staticTool.priority,
                                hashTags.content.as("hashTags"),
                                staticTool.toolCategory
                        )
                )
                .from(staticTool)
                .join(hashTags).on(staticTool.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(staticToolSearchDto))
                .orderBy(staticTool.idx.desc());

        long totalCount = query.fetchCount();
        List<StaticToolDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param staticToolSearchDto
     * @return
     */
    private BooleanExpression searchCondition(StaticToolSearchDto staticToolSearchDto) {
        BooleanExpression result;

        switch (staticToolSearchDto.getSearchType()) {
            case "TITLE":
                result = staticTool.title.contains(staticToolSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(staticToolSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = staticTool.content.contains(staticToolSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = staticTool.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(staticToolSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(staticToolSearchDto.getSearchKeyword())) {
            result = result.and(staticTool.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StaticToolDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StaticToolDto.class,
                                staticTool.idx,
                                staticTool.createdDate,
                                staticTool.createdByIdx,
                                staticTool.lastModifiedDate,
                                staticTool.lastModifiedByIdx,
                                staticTool.activeStatus,
                                staticTool.views,

                                staticTool.title,
                                hashTags.content.as("hashTags"),
                                staticTool.priority,
                                staticTool.hashTagsIdx,
                                staticTool.toolCategory,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                staticTool.toolInformationIdx,
                                ideInformation.ideName.as("ideName"),
                                staticTool.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                staticTool.compilerInformationIdx,
                                staticTool.content
                        )
                )
                .from(staticTool)
                .join(hashTags).on(staticTool.hashTagsIdx.eq(hashTags.idx))
                .join(toolInformation).on(staticTool.toolInformationIdx.eq(toolInformation.idx))
                .join(ideInformation).on(staticTool.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(staticTool.compilerInformationIdx.eq(compilerInformation.idx))
                .where(staticTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        staticTool.title
                )
                .distinct().from(staticTool)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public StaticTool findStaticToolPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                StaticTool.class,
                                staticTool.priority
                        )
                )
                .from(staticTool)
                .where(staticTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(staticTool)
                .set(staticTool.views, staticTool.views.add(1))
                .where(staticTool.idx.eq(idx))
                .execute();
    }
}