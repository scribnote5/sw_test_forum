package com.suresoft.sw_test_forum.storage.domain.enums;

public enum Category {
    STATIC_TEST("정적시험"),
    DYNAMIC_TEST("동적시험"),
    FINAL_TEST("프로젝트 정보 및 실사"),
    STUDY("스터디"),
    EDUCATION("교육 자료"),
    REFERENCE("참고 자료"),
    ETC("기타");

    private String category;

    private Category(String Category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}