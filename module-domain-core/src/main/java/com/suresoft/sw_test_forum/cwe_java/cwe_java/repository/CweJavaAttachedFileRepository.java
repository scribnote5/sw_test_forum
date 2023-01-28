package com.suresoft.sw_test_forum.cwe_java.cwe_java.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweJavaAttachedFileRepository extends JpaRepository<CweJavaAttachedFile, Long> {

}