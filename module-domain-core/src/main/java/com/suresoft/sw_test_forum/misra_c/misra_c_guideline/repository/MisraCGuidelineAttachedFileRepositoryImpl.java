package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuidelineAttachedFile.misraCGuidelineAttachedFile;

@Repository
@Transactional
public class MisraCGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public List<MisraCGuidelineAttachedFile> findAttachedFileByMisraCGuidelineIdx(long misraCGuidelineIdx) {
        return queryFactory
                .selectFrom(misraCGuidelineAttachedFile)
                .where(misraCGuidelineAttachedFile.misraCGuidelineIdx.eq(misraCGuidelineIdx))
                .orderBy(misraCGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByMisraCGuidelineIdx(long misraCGuidelineIdx) {
        return queryFactory
                .delete(misraCGuidelineAttachedFile)
                .where(misraCGuidelineAttachedFile.misraCGuidelineIdx.eq(misraCGuidelineIdx))
                .execute();
    }
}