package com.suresoft.sw_test_forum.cwe.cwe_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe.cwe_example.domain.QCweExampleComment.cweExampleComment;

@Repository
@Transactional
public class CweExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweExampleIdx
     * @return
     */
    public long countAllByCweExampleIdx(long cweExampleIdx) {
        return queryFactory
                .selectFrom(cweExampleComment)
                .where(cweExampleComment.cweExampleIdx.eq(cweExampleIdx))
                .fetchCount();
    }

    /**
     * 전체 삭제
     *
     * @param cweExampleIdx
     * @return
     */
    public long deleteAllByCweExampleIdx(long cweExampleIdx) {
        return queryFactory
                .delete(cweExampleComment)
                .where(cweExampleComment.cweExampleIdx.eq(cweExampleIdx))
                .execute();
    }
}