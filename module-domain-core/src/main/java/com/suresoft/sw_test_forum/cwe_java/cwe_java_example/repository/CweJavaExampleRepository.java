package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweJavaExampleRepository extends JpaRepository<CweJavaExample, Long> {
    public void deleteAllByCweJavaIdx(long cweJavaIdx);
}