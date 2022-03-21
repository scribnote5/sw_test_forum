package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.QToolchainAttachedFile.toolchainAttachedFile;

@Repository
@Transactional
public class ToolchainAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolchainAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolchainAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param toolchainIdx
     * @return
     */
    public List<ToolchainAttachedFile> findAttachedFileByToolchainIdx(long toolchainIdx) {
        return queryFactory
                .selectFrom(toolchainAttachedFile)
                .where(toolchainAttachedFile.toolchainIdx.eq(toolchainIdx))
                .orderBy(toolchainAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param toolchainIdx
     * @return
     */
    public long deleteAttachedFileByToolchainIdx(long toolchainIdx) {
        return queryFactory
                .delete(toolchainAttachedFile)
                .where(toolchainAttachedFile.toolchainIdx.eq(toolchainIdx))
                .execute();
    }
}