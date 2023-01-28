package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineLikeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;
import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.QJavaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineLike;

@Repository
@Transactional
public class JavaCodeConventionsGuidelineLikeRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsGuidelineLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsGuideline.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param username
     * @return
     */
    public JavaCodeConventionsGuidelineLikeDto findByUsernameAndJavaCodeConventionsGuidelineIdx(String username, long javaCodeConventionsGuidelineIdx) {
        return queryFactory.select(
                        Projections.bean(
                                JavaCodeConventionsGuidelineLikeDto.class,
                                javaCodeConventionsGuidelineLike.idx,
                                javaCodeConventionsGuidelineLike.createdDate,
                                javaCodeConventionsGuidelineLike.createdByIdx,
                                javaCodeConventionsGuidelineLike.lastModifiedDate,
                                javaCodeConventionsGuidelineLike.lastModifiedByIdx,
                                javaCodeConventionsGuidelineLike.activeStatus,
                                javaCodeConventionsGuidelineLike.views,

                                javaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineIdx
                        )
                )
                .from(javaCodeConventionsGuidelineLike)
                .join(user).on(user.username.eq(username))
                .where(javaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineIdx)
                        .and(javaCodeConventionsGuidelineLike.createdByIdx.eq(user.idx)))
                .fetchOne();
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsGuidelineLikeIdx
     * @return
     */
    public long deleteAllByJavaCodeConventionsGuidelineIdx(long javaCodeConventionsGuidelineLikeIdx) {
        return queryFactory
                .delete(javaCodeConventionsGuidelineLike)
                .where(javaCodeConventionsGuidelineLike.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineLikeIdx))
                .execute();
    }
}