package com.suresoft.sw_test_forum.admin_page.login_history.dto.mapper;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.LoginHistory;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistoryDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginHistoryMapper extends EntityMapper<LoginHistoryDto, LoginHistory> {
    LoginHistoryMapper INSTANCE = Mappers.getMapper(LoginHistoryMapper.class);
}