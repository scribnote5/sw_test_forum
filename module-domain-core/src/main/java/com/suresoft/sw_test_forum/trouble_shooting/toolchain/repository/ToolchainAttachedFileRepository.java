package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolchainAttachedFileRepository extends JpaRepository<ToolchainAttachedFile, Long> {

}