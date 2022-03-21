package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.QMisraCppComment.misraCppComment;

@Repository
@Transactional
public class MisraCppCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCppIdx
     * @return
     */
    public long countAllByMisraCppIdx(long misraCppIdx) {
        return queryFactory
                .selectFrom(misraCppComment)
                .where(misraCppComment.misraCppIdx.eq(misraCppIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCppIdx
     * @return
     */
    public long deleteAllByMisraCppIdx(long misraCppIdx) {
        return queryFactory
                .delete(misraCppComment)
                .where(misraCppComment.misraCppIdx.eq(misraCppIdx))
                .execute();
    }
}