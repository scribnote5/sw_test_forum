package com.suresoft.sw_test_forum.common.repository;

import com.suresoft.sw_test_forum.common.domain.TargetInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetInformationRepository extends JpaRepository<TargetInformation, Long> {

}