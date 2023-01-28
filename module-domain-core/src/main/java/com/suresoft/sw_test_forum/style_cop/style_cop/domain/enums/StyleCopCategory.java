package com.suresoft.sw_test_forum.style_cop.style_cop.domain.enums;

public enum StyleCopCategory {
    SPECIAL_RULES("Special Rules"),
    SPACING_RULES("Spacing Rules"),
    READABILITY_RULES("Readability Rules"),
    ORDERING_RULES("Ordering Rules"),
    NAMING_RULES("Naming Rules"),
    MAINTAINABILITY_RULES("Maintainability Rules"),
    LAYOUT_RULES("Layout Rules"),
    DOCUMENTATION_RULES("Documentation Rules"),
    ALTERNATIVE_RULES("Alternative Rules");

    private final String styleCopCategory;

    StyleCopCategory(String styleCopCategory) {
        this.styleCopCategory = styleCopCategory;
    }

    public String getStyleCopCategory() {
        return this.styleCopCategory;
    }
}