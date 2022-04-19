package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleCopGuidelineCommentRepository extends JpaRepository<StyleCopGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    List<StyleCopGuidelineComment> findAllByStyleCopGuidelineIdxOrderByIdxDesc(long styleCopGuidelineIdx);
}