package com.suresoft.sw_test_forum.style_cop.style_cop_example.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopExampleRepository extends JpaRepository<StyleCopExample, Long> {
    void deleteAllByStyleCopIdx(long styleCopIdx);
}