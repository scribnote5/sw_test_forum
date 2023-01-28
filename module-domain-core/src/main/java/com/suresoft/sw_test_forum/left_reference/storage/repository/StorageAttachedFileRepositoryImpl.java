package com.suresoft.sw_test_forum.left_reference.storage.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.left_reference.storage.domain.QStorageAttachedFile.storageAttachedFile;

@Repository
@Transactional
public class StorageAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StorageAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StorageAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param storageIdx
     * @return
     */
    public List<StorageAttachedFile> findAttachedFileByStorageIdx(long storageIdx) {
        return queryFactory
                .selectFrom(storageAttachedFile)
                .where(storageAttachedFile.storageIdx.eq(storageIdx))
                .orderBy(storageAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param storageIdx
     * @return
     */
    public long deleteAttachedFileByStorageIdx(long storageIdx) {
        return queryFactory
                .delete(storageAttachedFile)
                .where(storageAttachedFile.storageIdx.eq(storageIdx))
                .execute();
    }
}