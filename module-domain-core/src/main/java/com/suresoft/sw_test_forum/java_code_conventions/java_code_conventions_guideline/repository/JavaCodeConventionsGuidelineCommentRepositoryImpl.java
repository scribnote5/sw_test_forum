package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.QJavaCodeConventionsGuidelineComment.javaCodeConventionsGuidelineComment;

@Repository
@Transactional
public class JavaCodeConventionsGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public long countAllByJavaCodeConventionsGuidelineCIdx(long javaCodeConventionsGuidelineIdx) {
        return queryFactory
                .selectFrom(javaCodeConventionsGuidelineComment)
                .where(javaCodeConventionsGuidelineComment.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public long deleteAllByJavaCodeConventionsGuidelineIdx(long javaCodeConventionsGuidelineIdx) {
        return queryFactory
                .delete(javaCodeConventionsGuidelineComment)
                .where(javaCodeConventionsGuidelineComment.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineIdx))
                .execute();
    }
}