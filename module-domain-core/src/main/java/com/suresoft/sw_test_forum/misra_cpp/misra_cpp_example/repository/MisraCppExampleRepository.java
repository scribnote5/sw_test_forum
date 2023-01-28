package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCppExampleRepository extends JpaRepository<MisraCppExample, Long> {
    public void deleteAllByMisraCppIdx(long misraCppIdx);
}