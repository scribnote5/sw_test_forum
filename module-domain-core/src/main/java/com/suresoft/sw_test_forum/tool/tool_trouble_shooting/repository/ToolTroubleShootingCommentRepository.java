package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolTroubleShootingCommentRepository extends JpaRepository<ToolTroubleShootingComment, Long> {
    /**
     * 
     * @param toolTroubleShootingIdx
     * @return
     */
    List<ToolTroubleShootingComment> findAllByToolTroubleShootingIdxOrderByCreatedDateDesc(long toolTroubleShootingIdx);
}