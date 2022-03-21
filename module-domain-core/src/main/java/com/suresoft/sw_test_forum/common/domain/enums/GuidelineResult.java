package com.suresoft.sw_test_forum.common.domain.enums;

public enum GuidelineResult {
    COMPLETED("완료"),
    EXCLUDE("사전 제외"),
    EXCEPTION("예외 처리(사유서 처리)"),
    FALSE_ALARM("도구 오탐(False Alarm)"),
    FALSE_ALARM_PATCHED("도구 오탐(False Alarm) 패치 완료");

    private final String guidelineResult;

    GuidelineResult(String guidelineResult) {
        this.guidelineResult = guidelineResult;
    }

    public String getGuidelineResult() {
        return this.guidelineResult;
    }
}