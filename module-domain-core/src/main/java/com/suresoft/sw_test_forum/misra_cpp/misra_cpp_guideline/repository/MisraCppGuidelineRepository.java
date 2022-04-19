package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCppGuidelineRepository extends JpaRepository<MisraCppGuideline, Long> {
    public void deleteAllByMisraCppIdx(long misraCppIdx);
}