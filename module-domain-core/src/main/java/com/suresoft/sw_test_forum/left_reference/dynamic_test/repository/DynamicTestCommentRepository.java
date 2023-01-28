package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicTestCommentRepository extends JpaRepository<DynamicTestComment, Long> {
    /**
     * 리스트 조회
     *
     * @param dynamicTestIdx
     * @return
     */
    List<DynamicTestComment> findAllByDynamicTestIdxOrderByIdxDesc(long dynamicTestIdx);
}