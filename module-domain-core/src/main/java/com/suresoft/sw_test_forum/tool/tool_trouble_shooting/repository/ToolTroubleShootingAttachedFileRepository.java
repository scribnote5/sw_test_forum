package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolTroubleShootingAttachedFileRepository extends JpaRepository<ToolTroubleShootingAttachedFile, Long> {

}