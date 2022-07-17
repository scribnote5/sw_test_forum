package com.suresoft.sw_test_forum.cwe_java.cwe_java.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweJavaCommentRepository extends JpaRepository<CweJavaComment, Long> {
    /**
     * 리스트 조회
     *
     * @param cweJavaIdx
     * @return
     */
    List<CweJavaComment> findAllByCweJavaIdxOrderByIdxDesc(long cweJavaIdx);
}