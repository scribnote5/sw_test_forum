package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticTool;

@Repository
public interface StaticToolRepository extends JpaRepository<StaticTool, Long> {

}