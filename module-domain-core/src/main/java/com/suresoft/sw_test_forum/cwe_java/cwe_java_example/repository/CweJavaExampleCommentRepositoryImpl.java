package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.QCweJavaExampleComment.cweJavaExampleComment;

@Repository
@Transactional
public class CweJavaExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweJavaExampleIdx
     * @return
     */
    public long countAllByCweJavaExampleIdx(long cweJavaExampleIdx) {
        return queryFactory
                .selectFrom(cweJavaExampleComment)
                .where(cweJavaExampleComment.cweJavaExampleIdx.eq(cweJavaExampleIdx))
                .fetchCount();
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaExampleIdx
     * @return
     */
    public long deleteAllByCweJavaExampleIdx(long cweJavaExampleIdx) {
        return queryFactory
                .delete(cweJavaExampleComment)
                .where(cweJavaExampleComment.cweJavaExampleIdx.eq(cweJavaExampleIdx))
                .execute();
    }
}