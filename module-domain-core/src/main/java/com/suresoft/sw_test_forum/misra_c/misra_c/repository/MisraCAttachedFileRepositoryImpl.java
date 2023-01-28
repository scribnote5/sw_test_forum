package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.misra_c.misra_c.domain.QMisraCAttachedFile.misraCAttachedFile;

@Repository
@Transactional
public class MisraCAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    public List<MisraCAttachedFile> findAttachedFileByMisraCIdx(long misraCIdx) {
        return queryFactory
                .selectFrom(misraCAttachedFile)
                .where(misraCAttachedFile.misraCIdx.eq(misraCIdx))
                .orderBy(misraCAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param misraCIdx
     * @return
     */
    public long deleteAttachedFileByMisraCIdx(long misraCIdx) {
        return queryFactory
                .delete(misraCAttachedFile)
                .where(misraCAttachedFile.misraCIdx.eq(misraCIdx))
                .execute();
    }
}