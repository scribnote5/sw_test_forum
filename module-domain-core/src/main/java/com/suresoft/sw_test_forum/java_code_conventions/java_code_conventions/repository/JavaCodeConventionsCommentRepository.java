package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JavaCodeConventionsCommentRepository extends JpaRepository<JavaCodeConventionsComment, Long> {
    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    List<JavaCodeConventionsComment> findAllByJavaCodeConventionsIdxOrderByIdxDesc(long javaCodeConventionsIdx);
}