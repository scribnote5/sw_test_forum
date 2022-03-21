package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.QCweGuidelineAttachedFile.cweGuidelineAttachedFile;

@Repository
@Transactional
public class CweGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param cweGuidelineIdx
     * @return
     */
    public List<CweGuidelineAttachedFile> findAttachedFileByCweGuidelineIdx(long cweGuidelineIdx) {
        return queryFactory
                .selectFrom(cweGuidelineAttachedFile)
                .where(cweGuidelineAttachedFile.cweGuidelineIdx.eq(cweGuidelineIdx))
                .orderBy(cweGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param cweGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByCweGuidelineIdx(long cweGuidelineIdx) {
        return queryFactory
                .delete(cweGuidelineAttachedFile)
                .where(cweGuidelineAttachedFile.cweGuidelineIdx.eq(cweGuidelineIdx))
                .execute();
    }
}