package com.suresoft.sw_test_forum.tool.tool_configuration.repository;

import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolConfigurationCommentRepository extends JpaRepository<ToolConfigurationComment, Long> {
    /**
     * 
     * @param toolConfigurationIdx
     * @return
     */
    List<ToolConfigurationComment> findAllByToolConfigurationIdxOrderByCreatedDateDesc(long toolConfigurationIdx);
}