package com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.enums;

public enum FxCopBreakingChange {
    NON_BREAKING("Non Breaking"),
    BREAKING("Breaking"),
    CHANGEABLE("Changeable");

    private final String fxCopBreakingChange;

    FxCopBreakingChange(String fxCopBreakingChange) {
        this.fxCopBreakingChange = fxCopBreakingChange;
    }

    public String getFxCopBreakingChange() {
        return this.fxCopBreakingChange;
    }
}
