package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavaCodeConventionsExampleRepository extends JpaRepository<JavaCodeConventionsExample, Long> {
    void deleteAllByJavaCodeConventionsIdx(long javaCodeConventionsIdx);
}