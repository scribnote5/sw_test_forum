package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControllerTesterToolAttachedFileRepository extends JpaRepository<ControllerTesterToolAttachedFile, Long> {

}