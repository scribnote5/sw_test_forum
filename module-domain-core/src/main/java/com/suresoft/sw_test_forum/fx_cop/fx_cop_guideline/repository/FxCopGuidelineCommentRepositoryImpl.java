package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.QFxCopGuidelineComment.fxCopGuidelineComment;

@Repository
@Transactional
public class FxCopGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public long countAllByFxCopGuidelineCIdx(long fxCopGuidelineIdx) {
        return queryFactory
                .selectFrom(fxCopGuidelineComment)
                .where(fxCopGuidelineComment.fxCopGuidelineIdx.eq(fxCopGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public long deleteAllByFxCopGuidelineIdx(long fxCopGuidelineIdx) {
        return queryFactory
                .delete(fxCopGuidelineComment)
                .where(fxCopGuidelineComment.fxCopGuidelineIdx.eq(fxCopGuidelineIdx))
                .execute();
    }
}