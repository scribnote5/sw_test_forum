package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopGuidelineLikeRepository extends JpaRepository<StyleCopGuidelineLike, Long> {
    /**
     * styleCopGuideline 좋아요 갯수 조회
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public long countByStyleCopGuidelineIdx(long styleCopGuidelineIdx);
}