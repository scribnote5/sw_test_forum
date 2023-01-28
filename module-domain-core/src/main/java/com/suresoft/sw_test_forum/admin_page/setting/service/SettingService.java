package com.suresoft.sw_test_forum.admin_page.setting.service;

import com.suresoft.sw_test_forum.admin_page.setting.domain.Setting;
import com.suresoft.sw_test_forum.admin_page.setting.dto.SettingDto;
import com.suresoft.sw_test_forum.admin_page.setting.dto.mapper.SettingMapper;
import com.suresoft.sw_test_forum.admin_page.setting.repository.SettingRepository;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SettingService {
    private final SettingRepository settingRepository;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public SettingService(SettingRepository settingRepository, UserService userService) {
        this.settingRepository = settingRepository;
        this.userService = userService;
    }

    /**
     * 조회
     *
     * @return
     */
    public SettingDto findSetting(long idx) {
        SettingDto settingDto = SettingMapper.INSTANCE.toDto(settingRepository.findById(idx).orElse(new Setting()));

        // 권한 설정
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(settingDto.getCreatedByIdx());
        User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(settingDto.getLastModifiedByIdx());

        settingDto.setAccess(AuthorityUtil.isAccessInRootAndManager());
        settingDto.setCreatedByUser(createdByUser);
        settingDto.setLastModifiedByUser(lastModifiedByUser);

        return settingDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public SettingDto findSetting() {
        SettingDto settingDto = SettingMapper.INSTANCE.toDto(settingRepository.findById(1L).orElse(new Setting()));

        return settingDto;
    }

    /**
     * 수정
     *
     * @param idx
     * @param settingDto
     * @return
     */
    @Transactional
    public long updateSetting(long idx, SettingDto settingDto) {
        Setting persistSetting = settingRepository.getReferenceById(idx);
        Setting setting = SettingMapper.INSTANCE.toEntity(settingDto);

        persistSetting.update(setting);

        return settingRepository.save(persistSetting).getIdx();
    }
}