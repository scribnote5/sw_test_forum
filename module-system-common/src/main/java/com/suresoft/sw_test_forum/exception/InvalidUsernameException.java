package com.suresoft.sw_test_forum.exception;

import com.suresoft.sw_test_forum.error.ErrorCode;

public class InvalidUsernameException extends BusinessException {
    public InvalidUsernameException() {
        super(ErrorCode.INVALID_USERNAME);
    }
}