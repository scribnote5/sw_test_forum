package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.QCweJavaGuidelineLike.cweJavaGuidelineLike;

@Repository
@Transactional
public class CweJavaGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public CweJavaGuidelineLikeDto findByUsernameAndCweJavaGuidelineIdx(String username, long cweJavaGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                CweJavaGuidelineLikeDto.class,
                                cweJavaGuidelineLike.idx,
                                cweJavaGuidelineLike.createdDate,
                                cweJavaGuidelineLike.createdByIdx,
                                cweJavaGuidelineLike.lastModifiedDate,
                                cweJavaGuidelineLike.lastModifiedByIdx,
                                cweJavaGuidelineLike.activeStatus,
                                cweJavaGuidelineLike.views,

                                cweJavaGuidelineLike.cweJavaGuidelineIdx
                        )
                )
                .from(cweJavaGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(cweJavaGuidelineLike.cweJavaGuidelineIdx.eq(cweJavaGuidelineIdx)
                        .and(cweJavaGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaGuidelineLikeIdx
     * @return
     */
    public long deleteAllByCweJavaGuidelineIdx(long cweJavaGuidelineLikeIdx) {
        return queryFactory
                .delete(cweJavaGuidelineLike)
                .where(cweJavaGuidelineLike.cweJavaGuidelineIdx.eq(cweJavaGuidelineLikeIdx))
                .execute();
    }
}