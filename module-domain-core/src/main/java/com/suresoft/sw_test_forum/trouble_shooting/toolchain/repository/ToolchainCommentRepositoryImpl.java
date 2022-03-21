package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.QToolchainComment.toolchainComment;

@Repository
@Transactional
public class ToolchainCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolchainCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param toolchainIdx
     * @return
     */
    public long countAllByToolchainIdx(long toolchainIdx) {
        return queryFactory
                .selectFrom(toolchainComment)
                .where(toolchainComment.toolchainIdx.eq(toolchainIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param toolchainIdx
     * @return
     */
    public long deleteAllByToolchainIdx(long toolchainIdx) {
        return queryFactory
                .delete(toolchainComment)
                .where(toolchainComment.toolchainIdx.eq(toolchainIdx))
                .execute();
    }
}