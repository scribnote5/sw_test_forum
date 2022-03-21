package com.suresoft.sw_test_forum.cwe.cwe.domain.enums;

public enum CweLanguage {
    C("C"),
    CPP("C++"),
    C_CPP("C, C++"),
    JAVA("Java"),
    ALL("모든 언어");

    private final String cweLanguage;

    CweLanguage(String cweLanguage) {
        this.cweLanguage = cweLanguage;
    }

    public String getCweLanguage() {
        return this.cweLanguage;
    }
}