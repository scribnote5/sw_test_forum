package com.suresoft.sw_test_forum.fx_cop.fx_cop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.QFxCopComment.fxCopComment;

@Repository
@Transactional
public class FxCopCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param fxCopIdx
     * @return
     */
    public long countAllByFxCopIdx(long fxCopIdx) {
        return queryFactory
                .selectFrom(fxCopComment)
                .where(fxCopComment.fxCopIdx.eq(fxCopIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param fxCopIdx
     * @return
     */
    public long deleteAllByFxCopIdx(long fxCopIdx) {
        return queryFactory
                .delete(fxCopComment)
                .where(fxCopComment.fxCopIdx.eq(fxCopIdx))
                .execute();
    }
}