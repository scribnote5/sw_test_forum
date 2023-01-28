package com.suresoft.sw_test_forum.admin_page.data_history.domain.enums;

public enum AuditType {
    SELECT("조회"),
    INSERT("등록"),
    UPDATE("수정"),
    DELETE("삭제"),
    LIKE("좋아요"),
    CANCEL_LIKE("좋아요 취소"),
    ETC("기타");

    private String auditType;

    private AuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditType() {
        return this.auditType;
    }
}