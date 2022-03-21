package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuidelineAttachedFile.misraCppGuidelineAttachedFile;

@Repository
@Transactional
public class MisraCppGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public List<MisraCppGuidelineAttachedFile> findAttachedFileByMisraCppGuidelineIdx(long misraCppGuidelineIdx) {
        return queryFactory
                .selectFrom(misraCppGuidelineAttachedFile)
                .where(misraCppGuidelineAttachedFile.misraCppGuidelineIdx.eq(misraCppGuidelineIdx))
                .orderBy(misraCppGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByMisraCppGuidelineIdx(long misraCppGuidelineIdx) {
        return queryFactory
                .delete(misraCppGuidelineAttachedFile)
                .where(misraCppGuidelineAttachedFile.misraCppGuidelineIdx.eq(misraCppGuidelineIdx))
                .execute();
    }
}