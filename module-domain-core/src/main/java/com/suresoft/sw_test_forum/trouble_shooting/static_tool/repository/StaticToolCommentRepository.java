package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaticToolCommentRepository extends JpaRepository<StaticToolComment, Long> {
    /**
     * 
     * @param staticToolIdx
     * @return
     */
    List<StaticToolComment> findAllByStaticToolIdxOrderByCreatedDateDesc(long staticToolIdx);
}