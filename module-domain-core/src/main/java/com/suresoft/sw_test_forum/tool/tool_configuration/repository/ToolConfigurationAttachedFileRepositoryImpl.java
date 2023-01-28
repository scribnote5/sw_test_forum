package com.suresoft.sw_test_forum.tool.tool_configuration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.tool.tool_configuration.domain.QToolConfigurationAttachedFile.toolConfigurationAttachedFile;

@Repository
@Transactional
public class ToolConfigurationAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolConfigurationAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolConfigurationAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param toolConfigurationIdx
     * @return
     */
    public List<ToolConfigurationAttachedFile> findAttachedFileByConfigurationIdx(long toolConfigurationIdx) {
        return queryFactory
                .selectFrom(toolConfigurationAttachedFile)
                .where(toolConfigurationAttachedFile.toolConfigurationIdx.eq(toolConfigurationIdx))
                .orderBy(toolConfigurationAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param toolConfigurationIdx
     * @return
     */
    public long deleteAttachedFileByConfigurationIdx(long toolConfigurationIdx) {
        return queryFactory
                .delete(toolConfigurationAttachedFile)
                .where(toolConfigurationAttachedFile.toolConfigurationIdx.eq(toolConfigurationIdx))
                .execute();
    }
}