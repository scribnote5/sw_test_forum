package com.suresoft.sw_test_forum.common.domain.enums;

public enum ToolCategory {
    INSTALL_SETTING("설치 및 설정"),
    FUNCTION("기능"),
    INFORMATION("정보"),
    ETC("기타");

    private final String troubleShootingCategory;

    ToolCategory(String troubleShootingCategory) {
        this.troubleShootingCategory = troubleShootingCategory;
    }

    public String getGuidelineResult() {
        return this.troubleShootingCategory;
    }
}