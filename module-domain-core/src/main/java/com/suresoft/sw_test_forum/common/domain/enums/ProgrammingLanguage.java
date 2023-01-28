package com.suresoft.sw_test_forum.common.domain.enums;

public enum ProgrammingLanguage {
    C("C"),
    C_CPP("C++"),
    C_SHARP("C#"),
    JAVA("Java");

    private final String programmingLanguage;

    ProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getTargetToolName() {
        return this.programmingLanguage;
    }
}