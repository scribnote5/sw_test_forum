package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.QJavaCodeConventionsExampleComment.javaCodeConventionsExampleComment;

@Repository
@Transactional
public class JavaCodeConventionsExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param javaCodeConventionsExampleIdx
     * @return
     */
    public long countAllByJavaCodeConventionsExampleIdx(long javaCodeConventionsExampleIdx) {
        return queryFactory
                .selectFrom(javaCodeConventionsExampleComment)
                .where(javaCodeConventionsExampleComment.javaCodeConventionsExampleIdx.eq(javaCodeConventionsExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsExampleIdx
     * @return
     */
    public long deleteAllByJavaCodeConventionsExampleIdx(long javaCodeConventionsExampleIdx) {
        return queryFactory
                .delete(javaCodeConventionsExampleComment)
                .where(javaCodeConventionsExampleComment.javaCodeConventionsExampleIdx.eq(javaCodeConventionsExampleIdx))
                .execute();
    }
}