package com.suresoft.sw_test_forum.admin_page.notice.repository;

import com.suresoft.sw_test_forum.admin_page.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

}