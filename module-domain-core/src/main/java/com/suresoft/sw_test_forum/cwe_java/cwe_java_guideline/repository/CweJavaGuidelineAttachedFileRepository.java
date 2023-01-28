package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweJavaGuidelineAttachedFileRepository extends JpaRepository<CweJavaGuidelineAttachedFile, Long> {

}