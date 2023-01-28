package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxCopGuidelineLikeRepository extends JpaRepository<FxCopGuidelineLike, Long> {
    /**
     * fxCopGuideline 좋아요 갯수 조회
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public long countByFxCopGuidelineIdx(long fxCopGuidelineIdx);
}