package com.suresoft.sw_test_forum.misra_c.misra_c_example.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisraCExampleCommentRepository extends JpaRepository<MisraCExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCExampleIdx
     * @return
     */
    List<MisraCExampleComment> findAllByMisraCExampleIdxOrderByIdxDesc(long misraCExampleIdx);
}