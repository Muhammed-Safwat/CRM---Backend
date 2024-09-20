package com.gws.crm.common.handler;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorLogger = LoggerFactory.getLogger("appErrorLogger");

    private long getTransactionId() {
        return new Transition().getId();
    }

    private void logError(String message, Exception ex) {
        String transactionId = String.valueOf(getTransactionId());
        errorLogger.error("[Transaction ID: {}] {}", transactionId, message, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logError("Exception occurred: " + message, ex);
        return ApiResponseHandler.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = "Missing request parameter: " + ex.getParameterName();
        logError("Exception occurred: " + message, ex);
        return ApiResponseHandler.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = "Illegal argument: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = "Method not allowed: " + ex.getMethod();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.METHOD_NOT_ALLOWED, message);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        String message = "Resource not found: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(JwtException ex) {
        String message = "Token expired: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        String message = "Authentication credentials not found: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(MailSendException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMailSendException(MailSendException ex) {
        String message = "Failed to send email ";
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.SERVICE_UNAVAILABLE, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        String message = "An unexpected error occurred: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "Access Denied: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        String message = "Invalid credentials: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        String message = "Credentials expired: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleDisabledException(DisabledException ex) {
        String message = "Account is disabled: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(LockedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleLockedException(LockedException ex) {
        String message = "Account is locked: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String message = "User not found: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        String message = "Authentication failed: " + ex.getMessage();
        logError(message, ex);
        return ApiResponseHandler.error(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleNotFoundResourceException(NotFoundResourceException ex) {
        return ApiResponseHandler.notFound(ex.getMessage());
    }

}
