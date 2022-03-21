package com.suresoft.sw_test_forum.admin_page.login_history.domain.enums;

public enum LoginResultType {
    SUCCESS("성공"),
    FAIL("실패");

    private String type;

    private LoginResultType(String type) {
        this.type = type;
    }

    public String getLoginResultType() {
        return this.type;
    }
}