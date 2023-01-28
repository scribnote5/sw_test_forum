package com.suresoft.sw_test_forum.cwe.cwe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe.cwe.domain.QCweComment.cweComment;

@Repository
@Transactional
public class CweCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweIdx
     * @return
     */
    public long countAllByCweIdx(long cweIdx) {
        return queryFactory
                .selectFrom(cweComment)
                .where(cweComment.cweIdx.eq(cweIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param cweIdx
     * @return
     */
    public long deleteAllByCweIdx(long cweIdx) {
        return queryFactory
                .delete(cweComment)
                .where(cweComment.cweIdx.eq(cweIdx))
                .execute();
    }
}