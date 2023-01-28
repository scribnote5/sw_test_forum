package com.suresoft.sw_test_forum.admin_page.user.domain.enums;

public enum Position {
    // 앞에 있는 알파뱃은 사용자 페이지에서 정렬 순서를 의미한다.
    A_EXECUTIVES("임원"),
    B_PRINCIPAL_RESEARCH_ENGINEER("수석연구원"),
    C_SENIOR_RESEARCH_ENGINEER("전문연구원"),
    D_RESEARCH_ENGINEER("선임연구원"),
    E_ASSOCIATE_RESEARCH_ENGINEER("연구원"),
    F_GENERAL_MANAGER("부장"),
    G_DEPUTY_GENERAL_MANAGER("차장"),
    H_MANAGER("과장"),
    I_ASSISTANT_MANAGER("대리"),
    J_STAFF("사원"),
    K_ETC("기타");

    private String userType;

    private Position(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }
}