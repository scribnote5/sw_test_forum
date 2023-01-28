package com.suresoft.sw_test_forum.common.domain.enums;

public enum OperationEnvironment {
    HOST("HOST"),
    TARGET("TARGET");

    private final String operationEnvironment;

    OperationEnvironment(String operationEnvironment) {
        this.operationEnvironment = operationEnvironment;
    }

    public String getOperationEnvironment() {
        return this.operationEnvironment;
    }
}