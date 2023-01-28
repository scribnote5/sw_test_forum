package com.suresoft.sw_test_forum.tool.tool_usage_method.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.tool.tool_usage_method.domain.QToolUsageMethodComment.toolUsageMethodComment;

@Repository
@Transactional
public class ToolUsageMethodCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolUsageMethodCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param toolUsageMethodIdx
     * @return
     */
    public long countAllByToolUsageMethodIdx(long toolUsageMethodIdx) {
        return queryFactory
                .selectFrom(toolUsageMethodComment.toolUsageMethodComment)
                .where(toolUsageMethodComment.toolUsageMethodComment.toolUsageMethodIdx.eq(toolUsageMethodIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param toolUsageMethodIdx
     * @return
     */
    public long deleteAllByToolUsageMethodIdx(long toolUsageMethodIdx) {
        return queryFactory
                .delete(toolUsageMethodComment.toolUsageMethodComment)
                .where(toolUsageMethodComment.toolUsageMethodComment.toolUsageMethodIdx.eq(toolUsageMethodIdx))
                .execute();
    }
}