package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.enums;

public enum MisraCppCategory {
    REQUIRED("Required"),
    ADVISORY("Advisory"),
    DOCUMENT("Document");

    private final String misraCppCategory;

    MisraCppCategory(String misraCppCategory) {
        this.misraCppCategory = misraCppCategory;
    }

    public String getMisraCCategory() {
        return this.misraCppCategory;
    }
}