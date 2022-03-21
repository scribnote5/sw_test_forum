package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.QStaticToolAttachedFile.staticToolAttachedFile;

@Repository
@Transactional
public class StaticToolAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StaticToolAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StaticToolAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param staticToolIdx
     * @return
     */
    public List<StaticToolAttachedFile> findAttachedFileByStaticToolIdx(long staticToolIdx) {
        return queryFactory
                .selectFrom(staticToolAttachedFile)
                .where(staticToolAttachedFile.staticToolIdx.eq(staticToolIdx))
                .orderBy(staticToolAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param staticToolIdx
     * @return
     */
    public long deleteAttachedFileByStaticToolIdx(long staticToolIdx) {
        return queryFactory
                .delete(staticToolAttachedFile)
                .where(staticToolAttachedFile.staticToolIdx.eq(staticToolIdx))
                .execute();
    }
}