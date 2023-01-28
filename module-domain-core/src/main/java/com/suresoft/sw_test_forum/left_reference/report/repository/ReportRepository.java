package com.suresoft.sw_test_forum.left_reference.report.repository;

import com.suresoft.sw_test_forum.left_reference.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}