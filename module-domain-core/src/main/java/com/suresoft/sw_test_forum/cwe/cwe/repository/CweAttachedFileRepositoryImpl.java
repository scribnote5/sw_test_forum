package com.suresoft.sw_test_forum.cwe.cwe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.cwe.cwe.domain.QCweAttachedFile.cweAttachedFile;

@Repository
@Transactional
public class CweAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param cweIdx
     * @return
     */
    public List<CweAttachedFile> findAttachedFileByCweIdx(long cweIdx) {
        return queryFactory
                .selectFrom(cweAttachedFile)
                .where(cweAttachedFile.cweIdx.eq(cweIdx))
                .orderBy(cweAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param cweIdx
     * @return
     */
    public long deleteAttachedFileByCweIdx(long cweIdx) {
        return queryFactory
                .delete(cweAttachedFile)
                .where(cweAttachedFile.cweIdx.eq(cweIdx))
                .execute();
    }
}