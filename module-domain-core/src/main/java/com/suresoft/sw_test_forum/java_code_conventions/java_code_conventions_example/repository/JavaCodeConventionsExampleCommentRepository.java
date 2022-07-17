package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JavaCodeConventionsExampleCommentRepository extends JpaRepository<JavaCodeConventionsExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsExampleIdx
     * @return
     */
    List<JavaCodeConventionsExampleComment> findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(long javaCodeConventionsExampleIdx);
}