package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCppRepository extends JpaRepository<MisraCpp, Long> {

}