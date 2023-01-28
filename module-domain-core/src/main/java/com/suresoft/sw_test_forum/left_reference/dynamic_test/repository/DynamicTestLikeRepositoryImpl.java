package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTest;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.QDynamicTestLike.dynamicTestLike;

@Repository
@Transactional
public class DynamicTestLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DynamicTestLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DynamicTest.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public DynamicTestLikeDto findByUsernameAndDynamicTestIdx(String username, long dynamicTestIdx) {
        return queryFactory.select(
                        Projections.bean(
                                DynamicTestLikeDto.class,
                                dynamicTestLike.idx,
                                dynamicTestLike.createdDate,
                                dynamicTestLike.createdByIdx,
                                dynamicTestLike.lastModifiedDate,
                                dynamicTestLike.lastModifiedByIdx,
                                dynamicTestLike.activeStatus,
                                dynamicTestLike.views,

                                dynamicTestLike.dynamicTestIdx
                        )
                )
                .from(dynamicTestLike)
                .join(user).on(user.username.eq(username))
                .where(dynamicTestLike.dynamicTestIdx.eq(dynamicTestIdx)
                        .and(dynamicTestLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param dynamicTestLikeIdx
     * @return
     */
    public long deleteAllByDynamicTestIdx(long dynamicTestLikeIdx) {
        return queryFactory
                .delete(dynamicTestLike)
                .where(dynamicTestLike.dynamicTestIdx.eq(dynamicTestLikeIdx))
                .execute();
    }
}