package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FxCopExampleRepository extends JpaRepository<FxCopExample, Long> {
    void deleteAllByFxCopIdx(long fxCopIdx);
}