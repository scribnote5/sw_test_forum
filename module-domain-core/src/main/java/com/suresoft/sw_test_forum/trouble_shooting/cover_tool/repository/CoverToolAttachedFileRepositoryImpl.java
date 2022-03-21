package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.QCoverToolAttachedFile.coverToolAttachedFile;

@Repository
@Transactional
public class CoverToolAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CoverToolAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CoverToolAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param coverToolIdx
     * @return
     */
    public List<CoverToolAttachedFile> findAttachedFileByCoverToolIdx(long coverToolIdx) {
        return queryFactory
                .selectFrom(coverToolAttachedFile)
                .where(coverToolAttachedFile.coverToolIdx.eq(coverToolIdx))
                .orderBy(coverToolAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param coverToolIdx
     * @return
     */
    public long deleteAttachedFileByCoverToolIdx(long coverToolIdx) {
        return queryFactory
                .delete(coverToolAttachedFile)
                .where(coverToolAttachedFile.coverToolIdx.eq(coverToolIdx))
                .execute();
    }
}