package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QCompilerInformation.compilerInformation;

@Repository
@Transactional
public class CompilerInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CompilerInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CompilerInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CompilerInformation findCompilerInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                CompilerInformation.class,
                                compilerInformation.compilerName,
                                compilerInformation.compilerNote
                        )
                )
                .from(compilerInformation)
                .where(compilerInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 컴파일러 명 조회
     *
     * @return
     */
    public List<String> findDistinctCompilerNameByTableName(String tableName) {
        return queryFactory.select(
                        compilerInformation.compilerName
                )
                .distinct().from(compilerInformation)
                .where(compilerInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 컴파일러 비고 조회
     *
     * @return
     */
    public List<String> findDistinctCompilerNoteByTableName(String tableName) {
        return queryFactory.select(
                        compilerInformation.compilerNote
                )
                .distinct().from(compilerInformation)
                .where(compilerInformation.tableName.eq(tableName))
                .fetch();
    }
}