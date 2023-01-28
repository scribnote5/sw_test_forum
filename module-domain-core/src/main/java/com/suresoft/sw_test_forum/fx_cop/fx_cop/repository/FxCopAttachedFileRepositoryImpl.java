package com.suresoft.sw_test_forum.fx_cop.fx_cop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.QFxCopAttachedFile.fxCopAttachedFile;

@Repository
@Transactional
public class FxCopAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public FxCopAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(FxCopAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    public List<FxCopAttachedFile> findAttachedFileByFxCopIdx(long fxCopIdx) {
        return queryFactory
                .selectFrom(fxCopAttachedFile)
                .where(fxCopAttachedFile.fxCopIdx.eq(fxCopIdx))
                .orderBy(fxCopAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param fxCopIdx
     * @return
     */
    public long deleteAttachedFileByFxCopIdx(long fxCopIdx) {
        return queryFactory
                .delete(fxCopAttachedFile)
                .where(fxCopAttachedFile.fxCopIdx.eq(fxCopIdx))
                .execute();
    }
}