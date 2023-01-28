package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.QFxCopExampleComment.fxCopExampleComment;

@Repository
@Transactional
public class FxCopExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param fxCopExampleIdx
     * @return
     */
    public long countAllByFxCopExampleIdx(long fxCopExampleIdx) {
        return queryFactory
                .selectFrom(fxCopExampleComment)
                .where(fxCopExampleComment.fxCopExampleIdx.eq(fxCopExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param fxCopExampleIdx
     * @return
     */
    public long deleteAllByFxCopExampleIdx(long fxCopExampleIdx) {
        return queryFactory
                .delete(fxCopExampleComment)
                .where(fxCopExampleComment.fxCopExampleIdx.eq(fxCopExampleIdx))
                .execute();
    }
}