package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.QControllerTesterToolComment.controllerTesterToolComment;

@Repository
@Transactional
public class ControllerTesterToolCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ControllerTesterToolCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param controllerTesterToolIdx
     * @return
     */
    public long countAllByControllerTesterToolIdx(long controllerTesterToolIdx) {
        return queryFactory
                .selectFrom(controllerTesterToolComment)
                .where(controllerTesterToolComment.controllerTesterToolIdx.eq(controllerTesterToolIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param controllerTesterToolIdx
     * @return
     */
    public long deleteAllByControllerTesterToolIdx(long controllerTesterToolIdx) {
        return queryFactory
                .delete(controllerTesterToolComment)
                .where(controllerTesterToolComment.controllerTesterToolIdx.eq(controllerTesterToolIdx))
                .execute();
    }
}