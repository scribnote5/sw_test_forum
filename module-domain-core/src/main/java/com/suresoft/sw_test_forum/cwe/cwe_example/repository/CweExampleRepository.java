package com.suresoft.sw_test_forum.cwe.cwe_example.repository;

import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweExampleRepository extends JpaRepository<CweExample, Long> {
    public void deleteAllByCweIdx(long cweIdx);
}