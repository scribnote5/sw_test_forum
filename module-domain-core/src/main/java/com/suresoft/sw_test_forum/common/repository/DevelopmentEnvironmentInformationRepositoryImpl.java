package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.DevelopmentEnvironmentInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QDevelopmentEnvironmentInformation.developmentEnvironmentInformation;

@Repository
@Transactional
public class DevelopmentEnvironmentInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DevelopmentEnvironmentInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(DevelopmentEnvironmentInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public DevelopmentEnvironmentInformation findDevelopmentEnvironmentInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                DevelopmentEnvironmentInformation.class,
                                developmentEnvironmentInformation.developmentEnvironmentName
                        )
                )
                .from(developmentEnvironmentInformation)
                .where(developmentEnvironmentInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 개발 환경 이름 조회
     *
     * @return
     */
    public List<String> findDistinctDevelopmentEnvironmentNameByTableName(String tableName) {
        return queryFactory.select(
                        developmentEnvironmentInformation.developmentEnvironmentName
                )
                .distinct().from(developmentEnvironmentInformation)
                .where(developmentEnvironmentInformation.tableName.eq(tableName))
                .fetch();
    }
}