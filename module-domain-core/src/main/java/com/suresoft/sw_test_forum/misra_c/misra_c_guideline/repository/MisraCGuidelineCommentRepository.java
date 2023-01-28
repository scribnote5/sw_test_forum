package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisraCGuidelineCommentRepository extends JpaRepository<MisraCGuidelineComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCGuidelineIdx
     * @return
     */
    List<MisraCGuidelineComment> findAllByMisraCGuidelineIdxOrderByIdxDesc(long misraCGuidelineIdx);
}