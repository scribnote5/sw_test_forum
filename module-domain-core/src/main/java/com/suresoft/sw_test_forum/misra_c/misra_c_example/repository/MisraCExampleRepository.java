package com.suresoft.sw_test_forum.misra_c.misra_c_example.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCExampleRepository extends JpaRepository<MisraCExample, Long> {
    void deleteAllByMisraCIdx(long misraCIdx);
}