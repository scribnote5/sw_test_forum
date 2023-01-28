package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CweGuidelineCommentRepository extends JpaRepository<CweGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param cweGuidelineIdx
     * @return
     */
    List<CweGuidelineComment> findAllByCweGuidelineIdxOrderByIdxDesc(long cweGuidelineIdx);
}