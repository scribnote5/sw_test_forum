package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleCopRepository extends JpaRepository<StyleCop, Long> {

}