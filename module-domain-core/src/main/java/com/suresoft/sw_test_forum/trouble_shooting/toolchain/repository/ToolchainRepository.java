package com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository;

import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.Toolchain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolchainRepository extends JpaRepository<Toolchain, Long> {

}