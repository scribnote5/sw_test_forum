package com.suresoft.sw_test_forum.admin_page.setting.dto.mapper;

import com.suresoft.sw_test_forum.admin_page.setting.domain.Setting;
import com.suresoft.sw_test_forum.admin_page.setting.dto.SettingDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SettingMapper extends EntityMapper<SettingDto, Setting> {
    SettingMapper INSTANCE = Mappers.getMapper(SettingMapper.class);
}