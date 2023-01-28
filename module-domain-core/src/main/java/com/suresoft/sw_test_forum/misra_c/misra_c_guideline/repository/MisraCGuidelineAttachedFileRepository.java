package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCGuidelineAttachedFileRepository extends JpaRepository<MisraCGuidelineAttachedFile, Long> {

}