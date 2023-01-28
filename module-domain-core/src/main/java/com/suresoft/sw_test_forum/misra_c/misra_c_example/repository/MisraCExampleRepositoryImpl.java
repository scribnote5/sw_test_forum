package com.suresoft.sw_test_forum.misra_c.misra_c_example.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExample;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;
import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;
import static com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.QMisraCExample.misraCExample;

@Repository
@Transactional
public class MisraCExampleRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCExampleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCExample.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCExample> findAllByHighPriorityAsc(long misraCIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCExample.class,
                                misraCExample.idx,
                                misraCExample.createdDate,
                                misraCExample.createdByIdx,
                                misraCExample.activeStatus,
                                misraCExample.views,

                                misraCExample.title,
                                misraCExample.priority
                        )
                )
                .from(misraCExample)
                .where(misraCExample.priority.loe(3)
                        .and(misraCExample.misraCIdx.eq(misraCIdx)))
                .orderBy(misraCExample.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인할 때, 우선순위 높은 리스트 조회
     *
     * @return
     */
    public MisraCExample findAllPriorityByIdx(long idx, long misraCIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCExample.class,
                                misraCExample.priority
                        )
                )
                .from(misraCExample)
                .where(misraCExample.idx.eq(idx)
                        .and(misraCExample.misraCIdx.eq(misraCIdx)))
                .fetchOne();
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCExampleSearchDto
     * @return
     */
    public PageImpl<MisraCExampleDto> findAll(Pageable pageable, MisraCExampleSearchDto misraCExampleSearchDto) {
        JPQLQuery<MisraCExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCExampleDto.class,
                                misraCExample.idx,
                                misraCExample.createdDate,
                                misraCExample.createdByIdx,
                                misraCExample.activeStatus,
                                misraCExample.views,

                                misraCExample.misraCIdx,
                                misraCExample.title,
                                misraCExample.priority
                        )
                )
                .from(misraCExample)
                .where(searchCondition(misraCExampleSearchDto),
                        searchCondition(misraCExampleSearchDto.getMisraCIdx()))
                .orderBy(misraCExample.priority.asc(), misraCExample.idx.desc());

        long totalCount = query.fetchCount();
        List<MisraCExampleDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param misraCExampleSearchDto
     * @return
     */
    private BooleanExpression searchCondition(MisraCExampleSearchDto misraCExampleSearchDto) {
        BooleanExpression result;

        switch (misraCExampleSearchDto.getSearchType()) {
            case "TITLE":
                result = misraCExample.title.contains(misraCExampleSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = misraCExample.content.contains(misraCExampleSearchDto.getSearchKeyword());
                break;
            case "CREATED_BY":
                result = misraCExample.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(misraCExampleSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * MISRA C 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    public List<MisraCExampleDto> findAll(long misraCIdx) {
        JPQLQuery<MisraCExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCExampleDto.class,
                                misraCExample.idx,
                                misraCExample.createdDate,
                                misraCExample.createdByIdx,
                                misraCExample.activeStatus,
                                misraCExample.views,

                                misraCExample.misraCIdx,
                                misraCExample.title,
                                misraCExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCExample.content,
                                misraCExample.nonCompliantExample,
                                misraCExample.compliantExample,
                                misraCExample.badCasePositionList,
                                misraCExample.goodCasePositionList
                        )
                )
                .from(misraCExample)
                .join(toolInformation).on(misraCExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCExample.misraCIdx.eq(misraCIdx)
                        .and(misraCExample.priority.loe(3)))
                .orderBy(misraCExample.priority.asc());

        return query.fetch();
    }

    /**
     * MISRA C 읽기 페이지 일 때, 리스트 조회 조건
     *
     * @param misraCIdx
     * @return
     */
    private BooleanExpression searchCondition(long misraCIdx) {
        BooleanExpression result;

        if (misraCIdx == 0) {
            result = null;
        } else {
            result = misraCExample.misraCIdx.eq(misraCIdx);
        }

        return result;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCExampleDto findByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCExampleDto.class,
                                misraCExample.idx,
                                misraCExample.createdDate,
                                misraCExample.createdByIdx,
                                misraCExample.lastModifiedDate,
                                misraCExample.lastModifiedByIdx,
                                misraCExample.activeStatus,
                                misraCExample.views,

                                misraCExample.misraCIdx,
                                misraCExample.title,
                                misraCExample.priority,
                                toolInformation.toolName.as("toolName"),
                                toolInformation.toolNote.as("toolNote"),
                                misraCExample.toolInformationIdx,
                                compilerInformation.compilerName.as("compilerName"),
                                compilerInformation.compilerNote.as("compilerNote"),
                                misraCExample.compilerInformationIdx,
                                misraCExample.content,
                                misraCExample.nonCompliantExample,
                                misraCExample.compliantExample,
                                misraCExample.badCasePositionList,
                                misraCExample.goodCasePositionList
                        )
                )
                .from(misraCExample)
                .join(toolInformation).on(misraCExample.toolInformationIdx.eq(toolInformation.idx))
                .join(compilerInformation).on(misraCExample.compilerInformationIdx.eq(compilerInformation.idx))
                .where(misraCExample.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        /*
         * SELECT
         *  distinct misraC.title
         *
         */
        return queryFactory.select(
                        misraCExample.title
                )
                .distinct().from(misraCExample)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory.update(misraCExample)
                .set(misraCExample.views, misraCExample.views.add(1))
                .where(misraCExample.idx.eq(idx))
                .execute();
    }

    /**
     * MISRA C 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    public List<MisraCExampleDto> findAllWhenDelete(long misraCIdx) {
        JPQLQuery<MisraCExampleDto> query = queryFactory.select(
                        Projections.bean(
                                MisraCExampleDto.class,
                                misraCExample.idx
                        )
                )
                .from(misraCExample)
                .where(misraCExample.misraCIdx.eq(misraCIdx));

        return query.fetch();
    }
}