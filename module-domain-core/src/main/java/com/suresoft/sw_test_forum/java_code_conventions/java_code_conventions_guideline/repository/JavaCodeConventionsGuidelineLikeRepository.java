package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavaCodeConventionsGuidelineLikeRepository extends JpaRepository<JavaCodeConventionsGuidelineLike, Long> {
    /**
     * javaCodeConventionsGuideline 좋아요 갯수 조회
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public long countByJavaCodeConventionsGuidelineIdx(long javaCodeConventionsGuidelineIdx);
}