package com.suresoft.sw_test_forum.common.domain.enums;

public enum Frequency {
    HIGH("높음"),
    AVERAGE("보통"),
    LOW("낮음");

    private final String frequency;

    Frequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return this.frequency;
    }
}