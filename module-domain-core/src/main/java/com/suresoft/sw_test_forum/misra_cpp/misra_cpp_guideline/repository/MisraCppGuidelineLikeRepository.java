package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCppGuidelineLikeRepository extends JpaRepository<MisraCppGuidelineLike, Long> {
    /**
     * misraCppGuideline 좋아요 갯수 조회
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public long countByMisraCppGuidelineIdx(long misraCppGuidelineIdx);
}