package com.suresoft.sw_test_forum.left_reference.storage.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.storage.domain.Storage;
import com.suresoft.sw_test_forum.left_reference.storage.domain.enums.Category;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageSearchDto;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.left_reference.storage.domain.QStorage.storage;

@Repository
@Transactional
public class StorageRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StorageRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Storage.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StorageDto> findAllByHighPriorityAsc() {
        return queryFactory.select(
                        Projections.bean(
                                StorageDto.class,
                                storage.idx,
                                storage.createdDate,
                                storage.createdByIdx,
                                storage.activeStatus,
                                storage.views,

                                storage.title,
                                storage.priority,
                                storage.category
                        )
                )
                .from(storage)
                .where(storage.priority.loe(5))
                .orderBy(storage.priority.asc())
                .fetch();
    }

    /**
     * 우선순위 확인 할 때, 우선순위 높은 리스트 조회
     *
     * @param idx
     * @return
     */
    public Storage findAllByHighPriorityAscCheckPriority(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                Storage.class,
                                storage.priority
                        )
                )
                .from(storage)
                .where(storage.idx.eq(idx))
                .fetchOne();
    }

    /**
     * 우선수위 낮은 리스트 조회
     *
     * @param pageable
     * @param storageSearchDto
     * @return
     */
    public PageImpl<StorageDto> findAll(Pageable pageable, StorageSearchDto storageSearchDto) {
        JPQLQuery<StorageDto> query = queryFactory.select(
                        Projections.bean(
                                StorageDto.class,
                                storage.idx,
                                storage.createdDate,
                                storage.createdByIdx,
                                storage.activeStatus,
                                storage.views,

                                storage.title,
                                storage.priority,
                                storage.category
                        )
                )
                .from(storage)
                .where(searchCondition(storageSearchDto))
                .orderBy(storage.idx.desc());

        long totalCount = query.fetchCount();
        List<StorageDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param storageSearchDto
     * @return
     */
    private BooleanExpression searchCondition(StorageSearchDto storageSearchDto) {
        BooleanExpression result;

        switch (storageSearchDto.getSearchType()) {
            case "TITLE":
                result = storage.title.contains(storageSearchDto.getSearchKeyword());
                break;
            case "CONTENT":
                result = storage.content.contains(storageSearchDto.getSearchKeyword());
                break;
            case "CATEGORY":
                result = storage.category.eq(Category.valueOf(storageSearchDto.getSearchKeyword()));
                break;
            case "CREATED_BY":
                result = storage.createdByIdx.in(JPAExpressions
                        .select(user.idx)
                        .from(user)
                        .where(user.name.contains(storageSearchDto.getSearchKeyword())));
                break;
            default:
                result = null;
        }

        if (EmptyUtil.isEmpty(storageSearchDto.getSearchKeyword())) {
            result = result.and(storage.priority.goe(6));
        }

        return result;
    }

    /**
     * Auto Complete 제목 조회
     *
     * @return
     */
    public List<String> findDistinctTitle() {
        return queryFactory.select(
                        storage.title
                )
                .distinct().from(storage)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory
                .update(storage)
                .set(storage.views, storage.views.add(1))
                .where(storage.idx.eq(idx))
                .execute();
    }
}