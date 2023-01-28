package com.suresoft.sw_test_forum.admin_page.user.domain.enums;

public enum AuthorityType {
    ROOT("최고 관리자"),
    MANAGER("관리자"),
    GENERAL("일반 회원"),
    READER("읽기 회원"),
    NON_USER("비회원");

    private String authorityType;

    private AuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getAuthorityType() {
        return this.authorityType;
    }
}