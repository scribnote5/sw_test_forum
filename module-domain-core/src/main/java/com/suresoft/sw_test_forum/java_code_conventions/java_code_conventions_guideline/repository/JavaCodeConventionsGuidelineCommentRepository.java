package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JavaCodeConventionsGuidelineCommentRepository extends JpaRepository<JavaCodeConventionsGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    List<JavaCodeConventionsGuidelineComment> findAllByJavaCodeConventionsGuidelineIdxOrderByIdxDesc(long javaCodeConventionsGuidelineIdx);
}