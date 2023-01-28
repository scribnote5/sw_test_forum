package com.suresoft.sw_test_forum.tool.tool_usage_method.repository;

import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolUsageMethodAttachedFileRepository extends JpaRepository<ToolUsageMethodAttachedFile, Long> {

}