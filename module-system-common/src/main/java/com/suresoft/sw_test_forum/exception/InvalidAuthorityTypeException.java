package com.suresoft.sw_test_forum.exception;

import com.suresoft.sw_test_forum.error.ErrorCode;

public class InvalidAuthorityTypeException extends BusinessException {
    public InvalidAuthorityTypeException() {
        super(ErrorCode.INVALID_AUTHORITY_TYPE);
    }
}