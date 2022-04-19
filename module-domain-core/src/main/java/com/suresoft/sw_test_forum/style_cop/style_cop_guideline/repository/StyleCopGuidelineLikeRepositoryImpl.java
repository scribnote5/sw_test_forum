package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.QStyleCopGuidelineLike.styleCopGuidelineLike;

@Repository
@Transactional
public class StyleCopGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public StyleCopGuidelineLikeDto findByUsernameAndStyleCopGuidelineIdx(String username, long styleCopGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                StyleCopGuidelineLikeDto.class,
                                styleCopGuidelineLike.idx,
                                styleCopGuidelineLike.createdDate,
                                styleCopGuidelineLike.createdByIdx,
                                styleCopGuidelineLike.lastModifiedDate,
                                styleCopGuidelineLike.lastModifiedByIdx,
                                styleCopGuidelineLike.activeStatus,
                                styleCopGuidelineLike.views,

                                styleCopGuidelineLike.styleCopGuidelineIdx
                        )
                )
                .from(styleCopGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(styleCopGuidelineLike.styleCopGuidelineIdx.eq(styleCopGuidelineIdx)
                        .and(styleCopGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param styleCopGuidelineLikeIdx
     * @return
     */
    public long deleteAllByStyleCopGuidelineIdx(long styleCopGuidelineLikeIdx) {
        return queryFactory
                .delete(styleCopGuidelineLike)
                .where(styleCopGuidelineLike.styleCopGuidelineIdx.eq(styleCopGuidelineLikeIdx))
                .execute();
    }
}