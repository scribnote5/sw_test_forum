package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.QDynamicTestAttachedFile.dynamicTestAttachedFile;

@Repository
@Transactional
public class DynamicTestAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DynamicTestAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DynamicTestAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param dynamicTestIdx
     * @return
     */
    public List<DynamicTestAttachedFile> findAttachedFileByDynamicTestIdx(long dynamicTestIdx) {
        return queryFactory
                .selectFrom(dynamicTestAttachedFile)
                .where(dynamicTestAttachedFile.dynamicTestIdx.eq(dynamicTestIdx))
                .orderBy(dynamicTestAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param dynamicTestIdx
     * @return
     */
    public long deleteAttachedFileByDynamicTestIdx(long dynamicTestIdx) {
        return queryFactory
                .delete(dynamicTestAttachedFile)
                .where(dynamicTestAttachedFile.dynamicTestIdx.eq(dynamicTestIdx))
                .execute();
    }
}