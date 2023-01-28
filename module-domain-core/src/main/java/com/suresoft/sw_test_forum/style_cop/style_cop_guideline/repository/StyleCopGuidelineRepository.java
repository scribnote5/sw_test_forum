package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopGuidelineRepository extends JpaRepository<StyleCopGuideline, Long> {
    public void deleteAllByStyleCopIdx(long styleCopIdx);
}