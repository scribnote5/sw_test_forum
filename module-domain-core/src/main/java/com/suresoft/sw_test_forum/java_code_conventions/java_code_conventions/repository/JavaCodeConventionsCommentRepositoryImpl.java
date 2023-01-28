package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.QJavaCodeConventionsComment.javaCodeConventionsComment;

@Repository
@Transactional
public class JavaCodeConventionsCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public long countAllByJavaCodeConventionsIdx(long javaCodeConventionsIdx) {
        return queryFactory
                .selectFrom(javaCodeConventionsComment)
                .where(javaCodeConventionsComment.javaCodeConventionsIdx.eq(javaCodeConventionsIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public long deleteAllByJavaCodeConventionsIdx(long javaCodeConventionsIdx) {
        return queryFactory
                .delete(javaCodeConventionsComment)
                .where(javaCodeConventionsComment.javaCodeConventionsIdx.eq(javaCodeConventionsIdx))
                .execute();
    }
}