package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FxCopExampleCommentRepository extends JpaRepository<FxCopExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param fxCopExampleIdx
     * @return
     */
    List<FxCopExampleComment> findAllByFxCopExampleIdxOrderByIdxDesc(long fxCopExampleIdx);
}