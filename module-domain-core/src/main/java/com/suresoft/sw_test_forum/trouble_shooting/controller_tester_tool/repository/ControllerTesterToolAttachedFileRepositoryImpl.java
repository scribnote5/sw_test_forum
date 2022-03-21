package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.QControllerTesterToolAttachedFile.controllerTesterToolAttachedFile;

@Repository
@Transactional
public class ControllerTesterToolAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ControllerTesterToolAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ControllerTesterToolAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param controllerTesterToolIdx
     * @return
     */
    public List<ControllerTesterToolAttachedFile> findAttachedFileByControllerTesterToolIdx(long controllerTesterToolIdx) {
        return queryFactory
                .selectFrom(controllerTesterToolAttachedFile)
                .where(controllerTesterToolAttachedFile.controllerTesterToolIdx.eq(controllerTesterToolIdx))
                .orderBy(controllerTesterToolAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param controllerTesterToolIdx
     * @return
     */
    public long deleteAttachedFileByControllerTesterToolIdx(long controllerTesterToolIdx) {
        return queryFactory
                .delete(controllerTesterToolAttachedFile)
                .where(controllerTesterToolAttachedFile.controllerTesterToolIdx.eq(controllerTesterToolIdx))
                .execute();
    }
}