package com.suresoft.sw_test_forum.common.converter;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToActiveStatus implements Converter<String, ActiveStatus> {
    @Override
    public ActiveStatus convert(String source) {
        return ActiveStatus.valueOf(source.toUpperCase());
    }
}