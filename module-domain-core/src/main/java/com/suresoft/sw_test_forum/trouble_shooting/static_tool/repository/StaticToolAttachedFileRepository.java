package com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository;

import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticToolAttachedFileRepository extends JpaRepository<StaticToolAttachedFile, Long> {

}