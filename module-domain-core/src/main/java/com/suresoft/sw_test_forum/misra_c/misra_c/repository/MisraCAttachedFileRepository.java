package com.suresoft.sw_test_forum.misra_c.misra_c.repository;

import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisraCAttachedFileRepository extends JpaRepository<MisraCAttachedFile, Long> {

}