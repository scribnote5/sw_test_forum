package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicTestAttachedFileRepository extends JpaRepository<DynamicTestAttachedFile, Long> {

}