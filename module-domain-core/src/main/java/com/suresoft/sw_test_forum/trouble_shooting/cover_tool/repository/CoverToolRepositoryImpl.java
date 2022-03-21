package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverTool;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolSearchDto;
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
import static com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.QCoverTool.coverTool;

@Repository
@Transactional
public class CoverToolRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CoverToolRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CoverTool.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CoverToolDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                CoverToolDto.class,
                                coverTool.idx,
                                coverTool.createdDate,
                                coverTool.createdByIdx,
                                coverTool.activeStatus,
                                coverTool.views,

                                coverTool.title,
                                coverTool.priority,
                                hashTags.content.as("hashTags"),
                                coverTool.toolCategory
                        )
                )
                .from(coverTool)
                .join(hashTags).on(coverTool.hashTagsIdx.eq(hashTags.idx))
                .where(coverTool.priority.loe(5))
                .orderBy(coverTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public List<CoverToolDto> findAllByHighPriorityAscCheckPriority() {
        return queryFactory.select(
                        Projections.bean(
                                CoverToolDto.class,
                                coverTool.priority
                        )
                )
                .from(coverTool)
                .where(coverTool.priority.loe(5))
                .orderBy(coverTool.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param coverToolSearchDto
     * @return
     */
    public PageImpl<CoverToolDto> findAll(Pageable pageable, CoverToolSearchDto coverToolSearchDto) {
        JPQLQuery<CoverToolDto> query = queryFactory.select(
                        Projections.bean(
                                CoverToolDto.class,
                                coverTool.idx,
                                coverTool.createdDate,
                                coverTool.createdByIdx,
                                coverTool.activeStatus,
                                coverTool.views,

                                coverTool.title,
                                coverTool.priority,
                                hashTags.content.as("hashTags"),
                                coverTool.toolCategory
                        )
                )
                .from(coverTool)
                .join(hashTags).on(coverTool.hashTagsIdx.eq(hashTags.idx))
                .where(searchCondition(coverToolSearchDto))
                .orderBy(coverTool.idx.desc());

        long totalCount = query.fetchCount();
        List<CoverToolDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param coverToolSearchDto
     * @return
     */
    private BooleanExpression searchCondition(CoverToolSearchDto coverToolSearchDto) {
        BooleanExpression result;

        switch (coverToolSearchDto.getSearchType()) {
            case "TITLE":
                result = coverTool.title.contains(coverToolSearchDto.getSearchKeyword());
                break;
            case "HASH_TAGS":
                result = hashTags.content.contains(coverToolSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = coverTool.content.contains(coverToolSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = coverTool.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(coverToolSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(coverToolSearchDto.getSearchKeyword())) {
            result = result.and(coverTool.priority.goe(6));
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CoverToolDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CoverToolDto.class,
                                coverTool.idx,
                                coverTool.createdDate,
                                coverTool.createdByIdx,
                                coverTool.lastModifiedDate,
                                coverTool.lastModifiedByIdx,
                                coverTool.activeStatus,
                                coverTool.views,

                                coverTool.title,
                                hashTags.content.as("hashTags"),
                                coverTool.priority,
                                coverTool.hashTagsIdx,
                                coverTool.toolCategory,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                coverTool.toolInformationIdx,
                                ideInformation.ideName.as("ideName"),
                                coverTool.ideInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                coverTool.compilerInformationIdx,
                                coverTool.content
                        )
                )
                .from(coverTool)
                .join(hashTags).on(coverTool.hashTagsIdx.eq(hashTags.idx))
                .join(toolInformation).on(coverTool.toolInformationIdx.eq(toolInformation.idx))
                .join(ideInformation).on(coverTool.ideInformationIdx.eq(ideInformation.idx))
                .join(compilerInformation).on(coverTool.compilerInformationIdx.eq(compilerInformation.idx))
                .where(coverTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        coverTool.title
                )
                .distinct().from(coverTool)
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 조회
     *
     * @param idx
     * @return
     */
    public CoverTool findCoverToolPriorityByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CoverTool.class,
                                coverTool.priority
                        )
                )
                .from(coverTool)
                .where(coverTool.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(coverTool)
                .set(coverTool.views, coverTool.views.add(1))
                .where(coverTool.idx.eq(idx))
                .execute();
    }
}