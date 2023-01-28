package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweJavaGuidelineLikeRepository extends JpaRepository<CweJavaGuidelineLike, Long> {
    /**
     * cweJavaGuideline 좋아요 갯수 조회
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public long countByCweJavaGuidelineIdx(long cweJavaGuidelineIdx);
}