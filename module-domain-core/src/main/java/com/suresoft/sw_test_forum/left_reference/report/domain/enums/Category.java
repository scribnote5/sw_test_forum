package com.suresoft.sw_test_forum.left_reference.report.domain.enums;

public enum Category {
    PROPOSAL("제안서"),
    BUSINESS_REPORT("사업 계획서"),
    RELIABILITY_TEST_REPORT("신뢰성시험 보고서"),
    CONFERENCE("회의 자료"),
    ETC("기타 자료");

    private String category;

    private Category(String Category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}