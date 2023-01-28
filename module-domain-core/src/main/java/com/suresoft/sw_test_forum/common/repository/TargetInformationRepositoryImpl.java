package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.TargetInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QTargetInformation.targetInformation;

@Repository
@Transactional
public class TargetInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public TargetInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(TargetInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public TargetInformation findTargetInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                TargetInformation.class,
                                targetInformation.boardName,
                                targetInformation.architecture,
                                targetInformation.interfaceMethod,
                                targetInformation.debuggerName,
                                targetInformation.executableSize,
                                targetInformation.bit,
                                targetInformation.ramUsage,
                                targetInformation.ramFreeSize,
                                targetInformation.flashFreeSize
                        )
                )
                .from(targetInformation)
                .where(targetInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 보드 이름 조회
     *
     * @return
     */
    public List<String> findDistinctBoardNameByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.boardName
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 아키텍처 조회
     *
     * @return
     */
    public List<String> findDistinctArchitectureByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.architecture
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 타겟 인터페이스 조회
     *
     * @return
     */
    public List<String> findDistinctInterfaceMethodByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.interfaceMethod
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 디버거 조회
     *
     * @return
     */
    public List<String> findDistinctDebuggerByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.debuggerName
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete 실행 파일 크기
     *
     * @return
     */
    public List<String> findDistinctExecutableSizeByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.executableSize
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete bit
     *
     * @return
     */
    public List<String> findDistinctBitByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.bit
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete RAM 사용량
     *
     * @return
     */
    public List<String> findDistinctRamUsageByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.ramUsage
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete RAM 여유 공간
     *
     * @return
     */
    public List<String> findDistinctRamFreeSizeByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.ramFreeSize
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }

    /**
     * Auto Complete FLASH 여유 공간
     *
     * @return
     */
    public List<String> findDistinctFlashFreeSizeByTableName(String tableName) {
        return queryFactory.select(
                        targetInformation.flashFreeSize
                )
                .distinct().from(targetInformation)
                .where(targetInformation.tableName.eq(tableName))
                .fetch();
    }
}