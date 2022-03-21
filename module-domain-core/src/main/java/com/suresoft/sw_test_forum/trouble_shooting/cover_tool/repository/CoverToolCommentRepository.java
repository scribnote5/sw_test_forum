package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverToolCommentRepository extends JpaRepository<CoverToolComment, Long> {
    /**
     * 
     * @param staticToolIdx
     * @return
     */
    List<CoverToolComment> findAllByCoverToolIdxOrderByCreatedDateDesc(long coverToolIdx);
}