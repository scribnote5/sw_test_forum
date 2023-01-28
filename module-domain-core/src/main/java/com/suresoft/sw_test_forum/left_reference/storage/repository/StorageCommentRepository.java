package com.suresoft.sw_test_forum.left_reference.storage.repository;

import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageCommentRepository extends JpaRepository<StorageComment, Long> {
    /**
     * 리스트 조회
     *
     * @param storageIdx
     * @return
     */
    List<StorageComment> findAllByStorageIdxOrderByIdxDesc(long storageIdx);
}