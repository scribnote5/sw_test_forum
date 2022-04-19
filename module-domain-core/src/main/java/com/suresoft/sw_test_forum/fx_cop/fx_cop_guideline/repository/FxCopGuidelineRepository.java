package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxCopGuidelineRepository extends JpaRepository<FxCopGuideline, Long> {
    public void deleteAllByFxCopIdx(long fxCopIdx);
}