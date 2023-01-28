package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavaCodeConventionsAttachedFileRepository extends JpaRepository<JavaCodeConventionsAttachedFile, Long> {

}