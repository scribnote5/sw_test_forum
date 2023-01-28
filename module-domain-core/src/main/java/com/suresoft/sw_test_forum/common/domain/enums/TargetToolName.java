package com.suresoft.sw_test_forum.common.domain.enums;

public enum TargetToolName {
    STATIC("STATIC"),
    COVER("COVER"),
    CONTROLLER_TESTER("Controller Tester"),
    TOOLCHAIN("툴체인"),
    VPES("VPES");

    private final String targetToolName;

    TargetToolName(String targetToolName) {
        this.targetToolName = targetToolName;
    }

    public String getTargetToolName() {
        return this.targetToolName;
    }
}