package com.suresoft.sw_test_forum.cwe.cwe_example.repository;

import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweExampleCommentRepository extends JpaRepository<CweExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param cweExampleIdx
     * @return
     */
    List<CweExampleComment> findAllByCweExampleIdxOrderByIdxDesc(long cweExampleIdx);
}