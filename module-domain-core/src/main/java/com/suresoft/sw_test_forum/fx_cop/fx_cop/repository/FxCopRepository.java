package com.suresoft.sw_test_forum.fx_cop.fx_cop.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxCopRepository extends JpaRepository<FxCop, Long> {

}