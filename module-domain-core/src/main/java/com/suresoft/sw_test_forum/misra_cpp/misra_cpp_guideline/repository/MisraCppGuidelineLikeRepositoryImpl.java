package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuidelineLike.misraCppGuidelineLike;

@Repository
@Transactional
public class MisraCppGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public MisraCppGuidelineLikeDto findByUsernameAndMisraCppGuidelineIdx(String username, long misraCppGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                MisraCppGuidelineLikeDto.class,
                                misraCppGuidelineLike.idx,
                                misraCppGuidelineLike.createdDate,
                                misraCppGuidelineLike.createdByIdx,
                                misraCppGuidelineLike.lastModifiedDate,
                                misraCppGuidelineLike.lastModifiedByIdx,
                                misraCppGuidelineLike.activeStatus,
                                misraCppGuidelineLike.views,

                                misraCppGuidelineLike.misraCppGuidelineIdx
                        )
                )
                .from(misraCppGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(misraCppGuidelineLike.misraCppGuidelineIdx.eq(misraCppGuidelineIdx)
                        .and(misraCppGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param misraCppGuidelineLikeIdx
     * @return
     */
    public long deleteAllByMisraCppGuidelineIdx(long misraCppGuidelineLikeIdx) {
        return queryFactory
                .delete(misraCppGuidelineLike)
                .where(misraCppGuidelineLike.misraCppGuidelineIdx.eq(misraCppGuidelineLikeIdx))
                .execute();
    }
}