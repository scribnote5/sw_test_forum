package com.suresoft.sw_test_forum.common.domain.enums;

public enum ActiveStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String activeStatus;

    ActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getActiveStatus() {
        return this.activeStatus;
    }
}