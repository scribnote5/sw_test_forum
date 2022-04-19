package com.suresoft.sw_test_forum.style_cop.style_cop_example.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleCopExampleCommentRepository extends JpaRepository<StyleCopExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param styleCopExampleIdx
     * @return
     */
    List<StyleCopExampleComment> findAllByStyleCopExampleIdxOrderByIdxDesc(long styleCopExampleIdx);
}