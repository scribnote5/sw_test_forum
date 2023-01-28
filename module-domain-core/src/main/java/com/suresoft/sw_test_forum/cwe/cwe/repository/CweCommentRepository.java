package com.suresoft.sw_test_forum.cwe.cwe.repository;

import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweCommentRepository extends JpaRepository<CweComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCppIdx
     * @return
     */
    List<CweComment> findAllByCweIdxOrderByIdxDesc(long misraCppIdx);
}