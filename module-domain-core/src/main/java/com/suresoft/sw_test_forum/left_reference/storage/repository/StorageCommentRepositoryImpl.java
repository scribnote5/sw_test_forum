package com.suresoft.sw_test_forum.left_reference.storage.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.left_reference.storage.domain.QStorageComment.storageComment;

@Repository
@Transactional
public class StorageCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StorageCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StorageComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param storageIdx
     * @return
     */
    public long countAllByStorageIdx(long storageIdx) {
        return queryFactory
                .selectFrom(storageComment)
                .where(storageComment.storageIdx.eq(storageIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param storageIdx
     * @return
     */
    public long deleteAllByStorageIdx(long storageIdx) {
        return queryFactory
                .delete(storageComment)
                .where(storageComment.storageIdx.eq(storageIdx))
                .execute();
    }
}