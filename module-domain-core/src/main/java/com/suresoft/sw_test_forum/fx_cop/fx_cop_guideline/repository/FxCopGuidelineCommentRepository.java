package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FxCopGuidelineCommentRepository extends JpaRepository<FxCopGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    List<FxCopGuidelineComment> findAllByFxCopGuidelineIdxOrderByIdxDesc(long fxCopGuidelineIdx);
}