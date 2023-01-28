package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.QCweGuidelineLike.cweGuidelineLike;

@Repository
@Transactional
public class CweGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public CweGuidelineLikeDto findByUsernameAndCweGuidelineIdx(String username, long cweGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweGuidelineLikeDto.class,
                                cweGuidelineLike.idx,
                                cweGuidelineLike.createdDate,
                                cweGuidelineLike.createdByIdx,
                                cweGuidelineLike.lastModifiedDate,
                                cweGuidelineLike.lastModifiedByIdx,
                                cweGuidelineLike.activeStatus,
                                cweGuidelineLike.views,

                                cweGuidelineLike.cweGuidelineIdx
                        )
                )
                .from(cweGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(cweGuidelineLike.cweGuidelineIdx.eq(cweGuidelineIdx)
                        .and(cweGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param cweGuidelineLikeIdx
     * @return
     */
    public long deleteAllByCweGuidelineIdx(long cweGuidelineLikeIdx) {
        return queryFactory
                .delete(cweGuidelineLike)
                .where(cweGuidelineLike.cweGuidelineIdx.eq(cweGuidelineLikeIdx))
                .execute();
    }
}