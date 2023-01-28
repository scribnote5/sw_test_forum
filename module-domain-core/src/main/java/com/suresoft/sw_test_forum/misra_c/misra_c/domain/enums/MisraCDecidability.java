package com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums;

public enum MisraCDecidability {
    DECIDEABLE("Decidable"),
    UNDECIDEABLE("Undecidable");

    private final String misraCDecidability;

    MisraCDecidability(String misraCDecidability) {
        this.misraCDecidability = misraCDecidability;
    }

    public String getMisraCDecidability() {
        return this.misraCDecidability;
    }
}