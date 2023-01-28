package com.suresoft.sw_test_forum.left_reference.dynamic_test.repository;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicTestLikeRepository extends JpaRepository<DynamicTestLike, Long> {
    /**
     * dynamicTest 좋아요 갯수 조회
     *
     * @param dynamicTestIdx
     * @return
     */
    public long countByDynamicTestIdx(long dynamicTestIdx);
}