package com.suresoft.sw_test_forum.left_reference.storage.domain.enums;

public enum Category {
    STUDY("스터디"),
    FINAL_TEST("실사 후기"),
    EDUCATION("교육 자료"),
    CHECK_LIST("체크 리스트"),
    REFERENCE("참고 자료(가이드라인)"),
    ETC("기타 자료");

    private String category;

    private Category(String Category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}