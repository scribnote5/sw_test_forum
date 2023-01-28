package com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.enums;

public enum GuidelineResult {
    COMPLETED("수정 완료"),
    SOFTWARE_DEPENSIVE_CODE("방어 코드"),
    HARDWARE_FAILURE("하드웨어 고장"),
    INFINITE_LOOP("무한 반복문"),
    SPECIAL_HARDWARE_VALUE("특수한 하드웨어 값"),
    INVALID_HARDWARE_VALUE("잘못된 하드웨어 값"),
    SPECIAL_ENVIRONMENT("특수한 시험 환경"),
    REAL_TIME_DEGRADATION("탐침 코드로 실시간성 저하"),
    TOOL_ERROR("도구 오류"),
    OTHER_EXCEPTION("기타 예외 처리");

    private final String guidelineResult;

    GuidelineResult(String guidelineResult) {
        this.guidelineResult = guidelineResult;
    }

    public String getGuidelineResult() {
        return this.guidelineResult;
    }
}