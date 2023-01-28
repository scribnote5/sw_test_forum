package com.suresoft.sw_test_forum.common.converter;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import org.springframework.core.convert.converter.Converter;

public class StringToLoginResultType implements Converter<String, LoginResultType> {
    @Override
    public LoginResultType convert(String source) {
        return LoginResultType.valueOf(source.toUpperCase());
    }
}