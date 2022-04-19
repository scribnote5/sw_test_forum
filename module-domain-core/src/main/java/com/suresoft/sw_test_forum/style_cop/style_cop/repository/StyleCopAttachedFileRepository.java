package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopAttachedFileRepository extends JpaRepository<StyleCopAttachedFile, Long> {

}