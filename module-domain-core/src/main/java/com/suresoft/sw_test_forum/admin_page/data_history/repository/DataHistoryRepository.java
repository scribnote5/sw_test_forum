package com.suresoft.sw_test_forum.admin_page.data_history.repository;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.DataHistory;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DataHistoryRepository extends JpaRepository<DataHistory, Long> {
    /**
     * auditType에 따른 리스트 개수 조회
     *
     * @param auditType
     * @return
     */
    long countAllByAuditType(AuditType auditType);

    /**
     * 기간 내의 리스트 개수 조회
     *
     * @param start
     * @param end
     * @return
     */
    long countAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}