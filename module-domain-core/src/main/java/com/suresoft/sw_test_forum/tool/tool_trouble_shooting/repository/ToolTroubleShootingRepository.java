package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository;

import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShooting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolTroubleShootingRepository extends JpaRepository<ToolTroubleShooting, Long> {

}