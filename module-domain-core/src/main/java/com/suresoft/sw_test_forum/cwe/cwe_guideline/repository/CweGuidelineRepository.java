package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweGuidelineRepository extends JpaRepository<CweGuideline, Long> {
    public void deleteAllByCweIdx(long cweIdx);
}