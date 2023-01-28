package com.suresoft.sw_test_forum.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    /* General */
    INVALID_INPUT_VALUE(400, "C001", "입력 값이 유효하지 않습니다.(The input value is invalid.)"),
    INVALID_TYPE_VALUE(400, "C002", " 유효하지 않은 자료형입니다.(Invalid type value.)"),
    METHOD_NOT_ALLOWED(405, "C003", "HTTP Method가 허가되지 않았습니다.(The HTTP method is not allowed.)"),
    HANDLE_ACCESS_DENIED(403, "C004", "접근이 거부되었습니다.(Access is denied.)"),
    INTERNAL_SERVER_ERROR(500, "C005", "서버 오류가 발생하였습니다.(Internal server error.)"),
    CLIENT_ABORT_EXCEPTION(500, "C005", "Client Abort Exception 발생하였습니다.(Client Abort Exception.)"),

    /* File */
    FILE_NOT_EXIST(500, "F001", "파일이 존재하지 않습니다.(The file does not exist and maybe is deleted.)"),
    FILE_SIZE_EXCEEDED(500, "F002", "업로드하는 파일 크기가 200MB보다 작아야 합니다.(The upload file size must be less than 200MB.)"),
    FILE_TYPE_NOT_ALLOWED(500, "F003", "업로드하는 파일의 mime type을 지원하지 않습니다.(The upload file mime type is not supported.)"),
    FILE_NUMBER_EXCEEDED(500, "F004", "업로드하는 파일의 개수가 초과하였습니다.(The upload file number is exceeded.)"),

    /* User */
    INVALID_USERNAME(500, "U001", "아이디가 중복되었거나 아이디의 길이가 6보다 작거나 16보다 큽니다.(The ID is duplicated or ID must be more than 6 characters and less than 16 characters.)"),
    INVALID_AUTHORITY_TYPE(500, "U002", "잘못된 권한 유형 입니다.(The authority type is invalid.)"),
    AUTHENTICATION_FAIL(500, "U003", "인증 절차가 실패하였습니다.(The authentication process is failed.)"),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(500, "U004", "로그인 인증 절차에 실패하였습니다.(Bad credentials exception.)"),
    INACTIVE_AUTHENTICATION_SERVICE_EXCEPTION(500, "U005", "비활성화된 사용자 입니다.(User is inactive.)"),
    NON_USER_AUTHENTICATION_SERVICE_EXCEPTION(500, "U006", "비회원 사용자 입니다.(User is 'NON_USER' authority.)"),

    /* Priority */
    MAIN_PAGE_PRIORITY_DUPLICATE(500, "A001", "우선순위가 중복되었습니다.(The main page priority is duplicated.)");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

}