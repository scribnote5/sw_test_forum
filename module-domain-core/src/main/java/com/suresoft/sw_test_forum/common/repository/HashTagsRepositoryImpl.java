package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QHashTags.hashTags;

@Repository
@Transactional
public class HashTagsRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public HashTagsRepositoryImpl(JPAQueryFactory queryFactory) {
        super(HashTags.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public HashTags findHashTagsByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                HashTags.class,
                                hashTags.content
                        )
                )
                .from(hashTags)
                .where(hashTags.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 해시태그 조회
     *
     * @return
     */
    public List<String> findDistinctHashTagsByTableName(String tableName) {
        return queryFactory.select(
                        hashTags.content
                )
                .distinct().from(hashTags)
                .where(hashTags.tableName.eq(tableName)
                        .and(hashTags.content.isNotEmpty()))
                .fetch();
    }
}