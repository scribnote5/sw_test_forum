package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverToolAttachedFileRepository extends JpaRepository<CoverToolAttachedFile, Long> {

}