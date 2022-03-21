package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControllerTesterToolCommentRepository extends JpaRepository<ControllerTesterToolComment, Long> {
    /**
     * 
     * @param controllerTesterToolIdx
     * @return
     */
    List<ControllerTesterToolComment> findAllByControllerTesterToolIdxOrderByCreatedDateDesc(long controllerTesterToolIdx);
}