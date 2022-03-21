package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweGuidelineLikeRepository extends JpaRepository<CweGuidelineLike, Long> {
    /**
     * cweGuideline 좋아요 갯수 조회
     *
     * @param cweGuidelineIdx
     * @return
     */
    public long countByCweGuidelineIdx(long cweGuidelineIdx);
}