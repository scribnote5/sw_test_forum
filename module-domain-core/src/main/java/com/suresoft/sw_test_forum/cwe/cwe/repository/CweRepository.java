package com.suresoft.sw_test_forum.cwe.cwe.repository;

import com.suresoft.sw_test_forum.cwe.cwe.domain.Cwe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CweRepository extends JpaRepository<Cwe, Long> {

}