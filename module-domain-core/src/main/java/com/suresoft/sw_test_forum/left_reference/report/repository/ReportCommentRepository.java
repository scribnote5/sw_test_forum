package com.suresoft.sw_test_forum.left_reference.report.repository;

import com.suresoft.sw_test_forum.left_reference.report.domain.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {
    /**
     * 리스트 조회
     *
     * @param reportIdx
     * @return
     */
    List<ReportComment> findAllByReportIdxOrderByIdxDesc(long reportIdx);
}