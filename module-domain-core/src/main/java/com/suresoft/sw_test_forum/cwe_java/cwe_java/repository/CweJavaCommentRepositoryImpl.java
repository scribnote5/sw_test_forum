package com.suresoft.sw_test_forum.cwe_java.cwe_java.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.QCweJavaComment.cweJavaComment;

@Repository
@Transactional
public class CweJavaCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public long countAllByCweJavaIdx(long cweJavaIdx) {
        return queryFactory
                .selectFrom(cweJavaComment)
                .where(cweJavaComment.cweJavaIdx.eq(cweJavaIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaIdx
     * @return
     */
    public long deleteAllByCweJavaIdx(long cweJavaIdx) {
        return queryFactory
                .delete(cweJavaComment)
                .where(cweJavaComment.cweJavaIdx.eq(cweJavaIdx))
                .execute();
    }
}