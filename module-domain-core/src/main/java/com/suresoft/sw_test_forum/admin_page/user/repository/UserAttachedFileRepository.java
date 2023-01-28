package com.suresoft.sw_test_forum.admin_page.user.repository;

import com.suresoft.sw_test_forum.admin_page.user.domain.UserAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttachedFileRepository extends JpaRepository<UserAttachedFile, Long> {

}