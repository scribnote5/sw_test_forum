package com.suresoft.sw_test_forum.admin_page.user.domain.enums;

public enum UserStatus {
    IN_OFFICE("재직중"),
    RETIREE("퇴사");

    private String userStatus;

    private UserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatus() {
        return this.userStatus;
    }
}