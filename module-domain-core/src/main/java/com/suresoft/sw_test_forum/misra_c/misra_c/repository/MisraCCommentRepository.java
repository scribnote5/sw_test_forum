package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisraCCommentRepository extends JpaRepository<MisraCComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCIdx
     * @return
     */
    List<MisraCComment> findAllByMisraCIdxOrderByIdxDesc(long misraCIdx);
}