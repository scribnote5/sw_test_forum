package com.suresoft.sw_test_forum.tool.tool_usage_method.repository;

import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolUsageMethodCommentRepository extends JpaRepository<ToolUsageMethodComment, Long> {
    /**
     * 
     * @param toolUsageMethodIdx
     * @return
     */
    List<ToolUsageMethodComment> findAllByToolUsageMethodIdxOrderByCreatedDateDesc(long toolUsageMethodIdx);
}