package com.suresoft.sw_test_forum.admin_page.login_history.repository;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.LoginHistory;
import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    /**
     * 기간 내의 리스트 개수 조회
     *
     * @param start
     * @param end
     * @return
     */
    long countAllByCreatedDateBetweenAndLoginResultTypeIs(LocalDateTime start, LocalDateTime end, LoginResultType loginResultType);
}