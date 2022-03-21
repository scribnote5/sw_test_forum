package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverToolRepository extends JpaRepository<CoverTool, Long> {

}