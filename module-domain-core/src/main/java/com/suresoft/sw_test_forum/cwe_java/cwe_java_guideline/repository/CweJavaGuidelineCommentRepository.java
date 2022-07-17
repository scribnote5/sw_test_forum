package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweJavaGuidelineCommentRepository extends JpaRepository<CweJavaGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    List<CweJavaGuidelineComment> findAllByCweJavaGuidelineIdxOrderByIdxDesc(long cweJavaGuidelineIdx);
}