package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.QDynamicTestComment.dynamicTestComment;

@Repository
@Transactional
public class DynamicTestCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DynamicTestCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DynamicTestComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param dynamicTestIdx
     * @return
     */
    public long countAllByDynamicTestCIdx(long dynamicTestIdx) {
        return queryFactory
                .selectFrom(dynamicTestComment)
                .where(dynamicTestComment.dynamicTestIdx.eq(dynamicTestIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param dynamicTestIdx
     * @return
     */
    public long deleteAllByDynamicTestIdx(long dynamicTestIdx) {
        return queryFactory
                .delete(dynamicTestComment)
                .where(dynamicTestComment.dynamicTestIdx.eq(dynamicTestIdx))
                .execute();
    }
}