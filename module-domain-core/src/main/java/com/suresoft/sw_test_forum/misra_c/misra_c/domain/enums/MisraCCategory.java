package com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums;

public enum MisraCCategory {
    MANDATORY("Mandatory"),
    REQUIRED("Required"),
    ADVISORY("Advisory");

    private final String misraCCategory;

    MisraCCategory(String misraCCategory) {
        this.misraCCategory = misraCCategory;
    }

    public String getMisraCCategory() {
        return this.misraCCategory;
    }
}