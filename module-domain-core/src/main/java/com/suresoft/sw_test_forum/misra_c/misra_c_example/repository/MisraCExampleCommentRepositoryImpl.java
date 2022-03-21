package com.suresoft.sw_test_forum.misra_c.misra_c_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.QMisraCExampleComment.misraCExampleComment;

@Repository
@Transactional
public class MisraCExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCExampleIdx
     * @return
     */
    public long countAllByMisraCExampleIdx(long misraCExampleIdx) {
        return queryFactory
                .selectFrom(misraCExampleComment)
                .where(misraCExampleComment.misraCExampleIdx.eq(misraCExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCExampleIdx
     * @return
     */
    public long deleteAllByMisraCExampleIdx(long misraCExampleIdx) {
        return queryFactory
                .delete(misraCExampleComment)
                .where(misraCExampleComment.misraCExampleIdx.eq(misraCExampleIdx))
                .execute();
    }
}