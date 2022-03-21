package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCGuidelineLikeRepository extends JpaRepository<MisraCGuidelineLike, Long> {
    /**
     * misraCGuideline 좋아요 갯수 조회
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public long countByMisraCGuidelineIdx(long misraCGuidelineIdx);
}