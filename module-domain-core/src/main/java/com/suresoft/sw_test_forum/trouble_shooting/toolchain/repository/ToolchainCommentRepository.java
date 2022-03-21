package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolchainCommentRepository extends JpaRepository<ToolchainComment, Long> {
    /**
     * @param toolchainIdx
     * @return
     */
    List<ToolchainComment> findAllByToolchainIdxOrderByCreatedDateDesc(long toolchainIdx);
}