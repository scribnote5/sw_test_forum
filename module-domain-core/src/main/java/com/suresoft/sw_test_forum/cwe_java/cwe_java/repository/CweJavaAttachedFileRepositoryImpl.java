package com.suresoft.sw_test_forum.cwe_java.cwe_java.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.QCweJavaAttachedFile.cweJavaAttachedFile;

@Repository
@Transactional
public class CweJavaAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    public List<CweJavaAttachedFile> findAttachedFileByCweJavaIdx(long cweJavaIdx) {
        return queryFactory
                .selectFrom(cweJavaAttachedFile)
                .where(cweJavaAttachedFile.cweJavaIdx.eq(cweJavaIdx))
                .orderBy(cweJavaAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param cweJavaIdx
     * @return
     */
    public long deleteAttachedFileByCweJavaIdx(long cweJavaIdx) {
        return queryFactory
                .delete(cweJavaAttachedFile)
                .where(cweJavaAttachedFile.cweJavaIdx.eq(cweJavaIdx))
                .execute();
    }
}