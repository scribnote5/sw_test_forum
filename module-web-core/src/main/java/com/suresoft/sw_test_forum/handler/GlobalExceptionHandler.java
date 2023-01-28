package com.suresoft.sw_test_forum.handler;

import com.suresoft.sw_test_forum.error.ErrorCode;
import com.suresoft.sw_test_forum.error.ErrorResponse;
import com.suresoft.sw_test_forum.exception.BusinessException;
import com.suresoft.sw_test_forum.exception.FileNumberExceededException;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.exception.InactiveUserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 로그인 할 때, 비활성화된 사용자
     */
    @ExceptionHandler(InactiveUserException.class)
    protected ResponseEntity<ErrorResponse> InactiveUserException(InactiveUserException e) {
        log.error("InactiveUserException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 로그인 할 때, 아이디 불일치
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    protected ResponseEntity<ErrorResponse> InternalAuthenticationServiceException(Exception e) {
        log.error("InternalAuthenticationServiceException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION, ErrorCode.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 로그인 할 때, 잘못된 비밀번호를 여러번 입력으로 잠금(미구현)
     */
    @ExceptionHandler(LockedException.class)
    protected ResponseEntity<ErrorResponse> LockedException(Exception e) {
        log.error("LockedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NON_USER_AUTHENTICATION_SERVICE_EXCEPTION, ErrorCode.NON_USER_AUTHENTICATION_SERVICE_EXCEPTION.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 로그인 할 때, 비밀번호 불일치
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> BadCredentialsException(Exception e) {
        log.error("BadCredentialsException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION, ErrorCode.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @Valid binding error가 발생할 때 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribute bindingResult error가 발생할 때 발생
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE, e.getBindingResult());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type binding error가 발생할 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method를 호출 할 때 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);

        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    /**
     * multipart에서 설정한 file size보다 큰 파일이 업로드 되는 경우 발생
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("handleMaxUploadSizeExceededException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_SIZE_EXCEEDED);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * file이 존재하지 않는 경우 발생
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleFileNotExistException(Exception e) {
        log.error("handleFileTypeException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_NOT_EXIST, e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * file type이 위험하고 악의적인 것으로 판별되는 경우 발생
     */
    @ExceptionHandler(FileTypeException.class)
    protected ResponseEntity<ErrorResponse> handleFileTypeNotAllowedException(Exception e) {
        log.error("handleFileTypeException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_TYPE_NOT_ALLOWED, e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * file 개수가 초과되는 경우 발생
     */
    @ExceptionHandler(FileNumberExceededException.class)
    protected ResponseEntity<ErrorResponse> handleFileNumberExceededException(Exception e) {
        log.error("handleNumberExceededException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_NUMBER_EXCEEDED, e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 비즈니스 요구사항에 따른 Exception
     * 비즈니스 요구사항에 예외일 경우 BusinessException으로 통일성 있게 처리
     * 추가로 늘어날 수는 있지만 exception 개수를 최소화 해야함
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 그 밖에 발생하는 모든 예외 처리, Null Point Exception 등
     * 개발자가 직접 핸들링해서 다른 예외로 던지지 않으면 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 외부 애플리케이션에 의해서 tomcat 내부에서 발생하는 오류로 애플리케이션 실행에 영향은 없음
     * 현재 원인이 불분명한 상태
     */
    @ExceptionHandler(ClientAbortException.class)
    protected ResponseEntity<ErrorResponse> handleTestException(Exception e) {
        log.error("ClientAbortException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.CLIENT_ABORT_EXCEPTION);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
