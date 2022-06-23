package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavaCodeConventionsGuidelineRepository extends JpaRepository<JavaCodeConventionsGuideline, Long> {
    public void deleteAllByJavaCodeConventionsIdx(long JavaCodeConventionsIdx);
}