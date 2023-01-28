package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.QToolTroubleShootingComment.toolTroubleShootingComment;

@Repository
@Transactional
public class ToolTroubleShootingCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolTroubleShootingCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param toolTroubleShootingIdx
     * @return
     */
    public long countAllByToolTroubleShootingIdx(long toolTroubleShootingIdx) {
        return queryFactory
                .selectFrom(toolTroubleShootingComment.toolTroubleShootingComment)
                .where(toolTroubleShootingComment.toolTroubleShootingComment.toolTroubleShootingIdx.eq(toolTroubleShootingIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param toolTroubleShootingIdx
     * @return
     */
    public long deleteAllByToolTroubleShootingIdx(long toolTroubleShootingIdx) {
        return queryFactory
                .delete(toolTroubleShootingComment.toolTroubleShootingComment)
                .where(toolTroubleShootingComment.toolTroubleShootingComment.toolTroubleShootingIdx.eq(toolTroubleShootingIdx))
                .execute();
    }
}