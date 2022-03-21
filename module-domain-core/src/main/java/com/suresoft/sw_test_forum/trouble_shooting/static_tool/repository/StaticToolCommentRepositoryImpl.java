package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.QStaticToolComment.staticToolComment;

@Repository
@Transactional
public class StaticToolCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StaticToolCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param staticToolIdx
     * @return
     */
    public long countAllByStaticToolIdx(long staticToolIdx) {
        return queryFactory
                .selectFrom(staticToolComment)
                .where(staticToolComment.staticToolIdx.eq(staticToolIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param staticToolIdx
     * @return
     */
    public long deleteAllByStaticToolIdx(long staticToolIdx) {
        return queryFactory
                .delete(staticToolComment)
                .where(staticToolComment.staticToolIdx.eq(staticToolIdx))
                .execute();
    }
}