package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweJavaExampleCommentRepository extends JpaRepository<CweJavaExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param cweJavaExampleIdx
     * @return
     */
    List<CweJavaExampleComment> findAllByCweJavaExampleIdxOrderByIdxDesc(long cweJavaExampleIdx);
}