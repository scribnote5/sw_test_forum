package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuideline;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuidelineLike.misraCGuidelineLike;

@Repository
@Transactional
public class MisraCGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public MisraCGuidelineLikeDto findByUsernameAndMisraCGuidelineIdx(String username, long misraCGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCGuidelineLikeDto.class,
                                misraCGuidelineLike.idx,
                                misraCGuidelineLike.createdDate,
                                misraCGuidelineLike.createdByIdx,
                                misraCGuidelineLike.lastModifiedDate,
                                misraCGuidelineLike.lastModifiedByIdx,
                                misraCGuidelineLike.activeStatus,
                                misraCGuidelineLike.views,

                                misraCGuidelineLike.misraCGuidelineIdx
                        )
                )
                .from(misraCGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(misraCGuidelineLike.misraCGuidelineIdx.eq(misraCGuidelineIdx)
                        .and(misraCGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param misraCGuidelineLikeIdx
     * @return
     */
    public long deleteAllByMisraCGuidelineIdx(long misraCGuidelineLikeIdx) {
        return queryFactory
                .delete(misraCGuidelineLike)
                .where(misraCGuidelineLike.misraCGuidelineIdx.eq(misraCGuidelineLikeIdx))
                .execute();
    }
}