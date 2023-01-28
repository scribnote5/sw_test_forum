package com.suresoft.sw_test_forum.tool.tool_usage_method.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.tool.tool_usage_method.domain.QToolUsageMethodAttachedFile.toolUsageMethodAttachedFile;

@Repository
@Transactional
public class ToolUsageMethodAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolUsageMethodAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolUsageMethodAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param toolUsageMethodIdx
     * @return
     */
    public List<ToolUsageMethodAttachedFile> findAttachedFileByToolUsageMethodIdx(long toolUsageMethodIdx) {
        return queryFactory
                .selectFrom(toolUsageMethodAttachedFile.toolUsageMethodAttachedFile)
                .where(toolUsageMethodAttachedFile.toolUsageMethodAttachedFile.toolUsageMethodIdx.eq(toolUsageMethodIdx))
                .orderBy(toolUsageMethodAttachedFile.toolUsageMethodAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param toolUsageMethodIdx
     * @return
     */
    public long deleteAttachedFileByToolUsageMethodIdx(long toolUsageMethodIdx) {
        return queryFactory
                .delete(toolUsageMethodAttachedFile.toolUsageMethodAttachedFile)
                .where(toolUsageMethodAttachedFile.toolUsageMethodAttachedFile.toolUsageMethodIdx.eq(toolUsageMethodIdx))
                .execute();
    }
}