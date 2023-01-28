package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.QCweJavaGuidelineAttachedFile.cweJavaGuidelineAttachedFile;

@Repository
@Transactional
public class CweJavaGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public List<CweJavaGuidelineAttachedFile> findAttachedFileByCweJavaGuidelineIdx(long cweJavaGuidelineIdx) {
        return queryFactory
                .selectFrom(cweJavaGuidelineAttachedFile)
                .where(cweJavaGuidelineAttachedFile.cweJavaGuidelineIdx.eq(cweJavaGuidelineIdx))
                .orderBy(cweJavaGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByCweJavaGuidelineIdx(long cweJavaGuidelineIdx) {
        return queryFactory
                .delete(cweJavaGuidelineAttachedFile)
                .where(cweJavaGuidelineAttachedFile.cweJavaGuidelineIdx.eq(cweJavaGuidelineIdx))
                .execute();
    }
}