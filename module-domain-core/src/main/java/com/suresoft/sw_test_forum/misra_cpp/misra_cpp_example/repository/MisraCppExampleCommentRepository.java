package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisraCppExampleCommentRepository extends JpaRepository<MisraCppExampleComment, Long> {
    /**
     * 리스트 조회
     *
     * @param misraCppExampleIdx
     * @return
     */
    List<MisraCppExampleComment> findAllByMisraCppExampleIdxOrderByIdxDesc(long misraCppExampleIdx);
}