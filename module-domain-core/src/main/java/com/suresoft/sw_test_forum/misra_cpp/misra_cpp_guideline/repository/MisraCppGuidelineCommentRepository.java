package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisraCppGuidelineCommentRepository extends JpaRepository<MisraCppGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    List<MisraCppGuidelineComment> findAllByMisraCppGuidelineIdxOrderByIdxDesc(long misraCppGuidelineIdx);
}