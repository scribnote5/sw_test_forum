package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweGuidelineAttachedFileRepository extends JpaRepository<CweGuidelineAttachedFile, Long> {

}