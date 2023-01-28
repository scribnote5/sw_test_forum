package com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.enums;

public enum FxCopCategory {
    GLOBALIZATION("Globalization"),
    DESIGN("Design"),
    INTEROPERABILITY("Interoperability"),
    MAINTAINABILITY("Maintainability"),
    MOBILITY("Mobility"),
    NAMING("Naming"),
    PERFORMANCE("Performance"),
    PORTABILITY("Portability"),
    RELIABILITY("Reliability"),
    SECURITY("Security"),
    USAGE("Usage");

    private final String fxCopCategory;

    FxCopCategory(String fxCopCategory) {
        this.fxCopCategory = fxCopCategory;
    }

    public String getFxCopCategory() {
        return this.fxCopCategory;
    }
}