package com.suresoft.sw_test_forum.fx_cop.fx_cop.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FxCopCommentRepository extends JpaRepository<FxCopComment, Long> {
    /**
     * 리스트 조회
     *
     * @param fxCopIdx
     * @return
     */
    List<FxCopComment> findAllByFxCopIdxOrderByIdxDesc(long fxCopIdx);
}