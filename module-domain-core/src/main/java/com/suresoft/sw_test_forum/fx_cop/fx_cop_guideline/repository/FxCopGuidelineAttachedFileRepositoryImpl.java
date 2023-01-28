package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.QFxCopGuidelineAttachedFile.fxCopGuidelineAttachedFile;

@Repository
@Transactional
public class FxCopGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public List<FxCopGuidelineAttachedFile> findAttachedFileByFxCopGuidelineIdx(long fxCopGuidelineIdx) {
        return queryFactory
                .selectFrom(fxCopGuidelineAttachedFile)
                .where(fxCopGuidelineAttachedFile.fxCopGuidelineIdx.eq(fxCopGuidelineIdx))
                .orderBy(fxCopGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByFxCopGuidelineIdx(long fxCopGuidelineIdx) {
        return queryFactory
                .delete(fxCopGuidelineAttachedFile)
                .where(fxCopGuidelineAttachedFile.fxCopGuidelineIdx.eq(fxCopGuidelineIdx))
                .execute();
    }
}