package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCRepository extends JpaRepository<MisraC, Long> {

}