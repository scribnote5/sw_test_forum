package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.QMisraCppExampleComment.misraCppExampleComment;

@Repository
@Transactional
public class MisraCppExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCppExampleIdx
     * @return
     */
    public long countAllByMisraCppExampleIdx(long misraCppExampleIdx) {
        return queryFactory
                .selectFrom(misraCppExampleComment)
                .where(misraCppExampleComment.misraCppExampleIdx.eq(misraCppExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCppExampleIdx
     * @return
     */
    public long deleteAllByMisraCppExampleIdx(long misraCppExampleIdx) {
        return queryFactory
                .delete(misraCppExampleComment)
                .where(misraCppExampleComment.misraCppExampleIdx.eq(misraCppExampleIdx))
                .execute();
    }
}