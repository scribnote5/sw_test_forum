package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.QCoverToolComment.coverToolComment;

@Repository
@Transactional
public class CoverToolCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CoverToolCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param coverToolIdx
     * @return
     */
    public long countAllByCoverToolIdx(long coverToolIdx) {
        return queryFactory
                .selectFrom(coverToolComment)
                .where(coverToolComment.coverToolIdx.eq(coverToolIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param coverToolIdx
     * @return
     */
    public long deleteAllByCoverToolIdx(long coverToolIdx) {
        return queryFactory
                .delete(coverToolComment)
                .where(coverToolComment.coverToolIdx.eq(coverToolIdx))
                .execute();
    }
}