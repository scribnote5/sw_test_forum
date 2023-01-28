package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QProjectInformation.projectInformation;

@Repository
@Transactional
public class ProjectInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ProjectInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ProjectInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ProjectInformation findProjectInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                ProjectInformation.class,
                                projectInformation.projectName
                        )
                )
                .from(projectInformation)
                .where(projectInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 프로젝트 명 조회
     *
     * @return
     */
    public List<String> findDistinctProjectNameByTableName(String tableName) {
        return queryFactory.select(
                        projectInformation.projectName
                )
                .distinct().from(projectInformation)
                .where(projectInformation.tableName.eq(tableName))
                .fetch();
    }
}