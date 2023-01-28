package com.suresoft.sw_test_forum.tool.tool_configuration.repository;

import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolConfigurationAttachedFileRepository extends JpaRepository<ToolConfigurationAttachedFile, Long> {

}