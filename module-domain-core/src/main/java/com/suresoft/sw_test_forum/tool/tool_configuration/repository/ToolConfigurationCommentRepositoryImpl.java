package com.suresoft.sw_test_forum.tool.tool_configuration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.tool.tool_configuration.domain.QToolConfigurationComment.toolConfigurationComment;

@Repository
@Transactional
public class ToolConfigurationCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolConfigurationCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param toolConfigurationIdx
     * @return
     */
    public long countAllByConfigurationIdx(long toolConfigurationIdx) {
        return queryFactory
                .selectFrom(toolConfigurationComment)
                .where(toolConfigurationComment.toolConfigurationIdx.eq(toolConfigurationIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param toolConfigurationIdx
     * @return
     */
    public long deleteAllByConfigurationIdx(long toolConfigurationIdx) {
        return queryFactory
                .delete(toolConfigurationComment)
                .where(toolConfigurationComment.toolConfigurationIdx.eq(toolConfigurationIdx))
                .execute();
    }
}