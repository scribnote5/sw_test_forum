package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.QFxCopGuidelineLike.fxCopGuidelineLike;

@Repository
@Transactional
public class FxCopGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public FxCopGuidelineLikeDto findByUsernameAndFxCopGuidelineIdx(String username, long fxCopGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                FxCopGuidelineLikeDto.class,
                                fxCopGuidelineLike.idx,
                                fxCopGuidelineLike.createdDate,
                                fxCopGuidelineLike.createdByIdx,
                                fxCopGuidelineLike.lastModifiedDate,
                                fxCopGuidelineLike.lastModifiedByIdx,
                                fxCopGuidelineLike.activeStatus,
                                fxCopGuidelineLike.views,

                                fxCopGuidelineLike.fxCopGuidelineIdx
                        )
                )
                .from(fxCopGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(fxCopGuidelineLike.fxCopGuidelineIdx.eq(fxCopGuidelineIdx)
                        .and(fxCopGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param fxCopGuidelineLikeIdx
     * @return
     */
    public long deleteAllByFxCopGuidelineIdx(long fxCopGuidelineLikeIdx) {
        return queryFactory
                .delete(fxCopGuidelineLike)
                .where(fxCopGuidelineLike.fxCopGuidelineIdx.eq(fxCopGuidelineLikeIdx))
                .execute();
    }
}