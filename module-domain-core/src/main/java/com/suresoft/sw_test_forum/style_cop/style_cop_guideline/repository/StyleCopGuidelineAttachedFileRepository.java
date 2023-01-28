package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopGuidelineAttachedFileRepository extends JpaRepository<StyleCopGuidelineAttachedFile, Long> {

}