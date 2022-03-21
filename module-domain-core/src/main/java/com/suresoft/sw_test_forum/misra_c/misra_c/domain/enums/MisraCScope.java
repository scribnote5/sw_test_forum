package com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums;

public enum MisraCScope {
    SYSTEM("System"),
    TRANSLATION_UNIT("Translation Unit");

    private final String misraCScope;

    MisraCScope(String misraCScope) {
        this.misraCScope = misraCScope;
    }

    public String getMisraCScope() {
        return this.misraCScope;
    }
}