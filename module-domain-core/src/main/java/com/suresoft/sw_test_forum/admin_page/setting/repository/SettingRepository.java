package com.suresoft.sw_test_forum.admin_page.setting.repository;

import com.suresoft.sw_test_forum.admin_page.setting.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

}