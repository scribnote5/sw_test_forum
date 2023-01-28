package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.QToolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile;

@Repository
@Transactional
public class ToolTroubleShootingAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolTroubleShootingAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolTroubleShootingAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param toolTroubleShootingIdx
     * @return
     */
    public List<ToolTroubleShootingAttachedFile> findAttachedFileByTroubleShootingIdx(long toolTroubleShootingIdx) {
        return queryFactory
                .selectFrom(toolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile)
                .where(toolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile.toolTroubleShootingIdx.eq(toolTroubleShootingIdx))
                .orderBy(toolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param toolTroubleShootingIdx
     * @return
     */
    public long deleteAttachedFileByTroubleShootingIdx(long toolTroubleShootingIdx) {
        return queryFactory
                .delete(toolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile)
                .where(toolTroubleShootingAttachedFile.toolTroubleShootingAttachedFile.toolTroubleShootingIdx.eq(toolTroubleShootingIdx))
                .execute();
    }
}