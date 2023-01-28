package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QToolInformation.toolInformation;

@Repository
@Transactional
public class ToolInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ToolInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ToolInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolInformation findToolInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ToolInformation.class,
                                toolInformation.toolName,
                                toolInformation.toolNote
                        )
                )
                .from(toolInformation)
                .where(toolInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 도구 명 조회
     *
     * @return
     */
    public List<String> findDistinctToolNameByTableName(String tableName) {
        return queryFactory.select(
                        toolInformation.toolName
                )
                .distinct().from(toolInformation)
                .where(toolInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 도구 비고 조회
     *
     * @return
     */
    public List<String> findDistinctToolNoteByTableName(String tableName) {
        return queryFactory.select(
                        toolInformation.toolNote
                )
                .distinct().from(toolInformation)
                .where(toolInformation.tableName.eq(tableName))
                .fetch();
    }
}