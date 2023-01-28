package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_c.misra_c.domain.QMisraCComment.misraCComment;

@Repository
@Transactional
public class MisraCCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCIdx
     * @return
     */
    public long countAllByMisraCIdx(long misraCIdx) {
        return queryFactory
                .selectFrom(misraCComment)
                .where(misraCComment.misraCIdx.eq(misraCIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCIdx
     * @return
     */
    public long deleteAllByMisraCIdx(long misraCIdx) {
        return queryFactory
                .delete(misraCComment)
                .where(misraCComment.misraCIdx.eq(misraCIdx))
                .execute();
    }
}