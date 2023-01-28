package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweJavaGuidelineRepository extends JpaRepository<CweJavaGuideline, Long> {
    public void deleteAllByCweJavaIdx(long cweJavaIdx);
}