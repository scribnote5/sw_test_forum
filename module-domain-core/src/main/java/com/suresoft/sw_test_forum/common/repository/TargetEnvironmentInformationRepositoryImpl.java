package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.TargetEnvironmentInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QTargetEnvironmentInformation.targetEnvironmentInformation;

@Repository
@Transactional
public class TargetEnvironmentInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public TargetEnvironmentInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(TargetEnvironmentInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public TargetEnvironmentInformation findTargetEnvironmentInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                TargetEnvironmentInformation.class,
                                targetEnvironmentInformation.targetEnvironmentName
                        )
                )
                .from(targetEnvironmentInformation)
                .where(targetEnvironmentInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 타겟 환경 이름 조회
     *
     * @return
     */
    public List<String> findDistinctTargetEnvironmentNameByTableName(String tableName) {
        return queryFactory.select(
                        targetEnvironmentInformation.targetEnvironmentName
                )
                .distinct().from(targetEnvironmentInformation)
                .where(targetEnvironmentInformation.tableName.eq(tableName))
                .fetch();
    }
}