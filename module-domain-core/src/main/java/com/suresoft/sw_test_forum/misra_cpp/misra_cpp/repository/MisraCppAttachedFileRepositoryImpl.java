package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.QMisraCppAttachedFile.misraCppAttachedFile;

@Repository
@Transactional
public class MisraCppAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    public List<MisraCppAttachedFile> findAttachedFileByMisraCppIdx(long misraCppIdx) {
        return queryFactory
                .selectFrom(misraCppAttachedFile)
                .where(misraCppAttachedFile.misraCppIdx.eq(misraCppIdx))
                .orderBy(misraCppAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param misraCppIdx
     * @return
     */
    public long deleteAttachedFileByMisraCppIdx(long misraCppIdx) {
        return queryFactory
                .delete(misraCppAttachedFile)
                .where(misraCppAttachedFile.misraCppIdx.eq(misraCppIdx))
                .execute();
    }
}