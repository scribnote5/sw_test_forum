package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleCopCommentRepository extends JpaRepository<StyleCopComment, Long> {
    /**
     * 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    List<StyleCopComment> findAllByStyleCopIdxOrderByIdxDesc(long styleCopIdx);
}