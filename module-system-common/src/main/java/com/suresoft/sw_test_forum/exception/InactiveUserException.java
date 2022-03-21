package com.suresoft.sw_test_forum.exception;

import com.suresoft.sw_test_forum.error.ErrorCode;

public class InactiveUserException extends BusinessException {
    public InactiveUserException() {super(ErrorCode.INACTIVE_AUTHENTICATION_SERVICE_EXCEPTION); }
}