package com.gws.crm.common.handler;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.InvalidPhoneNumberException;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorLogger = LoggerFactory.getLogger("appErrorLogger");

    private long getTransactionId() {
        return new Transition().getId();
    }

    private void logError(Exception ex) {
        String transactionId = String.valueOf(getTransactionId());
        errorLogger.error("[Transaction ID: {}] Unexpected error", transactionId, ex);
    }

    private ResponseEntity<ApiResponse<Void>> buildError(HttpStatus status, String userMessage, Exception ex) {
        logError(ex);
        return ApiResponseHandler.error(status, userMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String userMessage = "Invalid request data, please check your input.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleInvalidPhoneNumberException(InvalidPhoneNumberException ex) {
        String userMessage = "The phone number format is invalid.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException ex) {
        String userMessage = "Required request parameter is missing.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String userMessage = "Invalid request data.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String userMessage = "This method is not allowed.";
        return buildError(HttpStatus.METHOD_NOT_ALLOWED, userMessage, ex);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        String userMessage = "The requested resource was not found.";
        return buildError(HttpStatus.NOT_FOUND, userMessage, ex);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(JwtException ex) {
        String userMessage = "Your session has expired, please login again.";
        return buildError(HttpStatus.UNAUTHORIZED, userMessage, ex);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        String userMessage = "Authentication credentials not found.";
        return buildError(HttpStatus.UNAUTHORIZED, userMessage, ex);
    }

    @ExceptionHandler(MailSendException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMailSendException(MailSendException ex) {
        String userMessage = "Failed to send email. Please try again later.";
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, userMessage, ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        String userMessage = "Something went wrong, please try again later.";
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, userMessage, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        String userMessage = "You do not have permission to access this resource.";
        return buildError(HttpStatus.FORBIDDEN, userMessage, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        String userMessage = "Invalid username or password.";
        return buildError(HttpStatus.UNAUTHORIZED, userMessage, ex);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        String userMessage = "Your credentials have expired, please login again.";
        return buildError(HttpStatus.UNAUTHORIZED, userMessage, ex);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleDisabledException(DisabledException ex) {
        String userMessage = "Your account is disabled, please contact support.";
        return buildError(HttpStatus.FORBIDDEN, userMessage, ex);
    }

    @ExceptionHandler(LockedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleLockedException(LockedException ex) {
        String userMessage = "Your account is locked, please contact support.";
        return buildError(HttpStatus.FORBIDDEN, userMessage, ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String userMessage = "User not found.";
        return buildError(HttpStatus.NOT_FOUND, userMessage, ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        String userMessage = "Authentication failed.";
        return buildError(HttpStatus.UNAUTHORIZED, userMessage, ex);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleNotFoundResourceException(NotFoundResourceException ex) {
        String userMessage = "The requested resource was not found.";
        return buildError(HttpStatus.NOT_FOUND, userMessage, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String userMessage = "Invalid request format, please check your input.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String userMessage = "Unsupported content type.";
        return buildError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, userMessage, ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rootMessage = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";
        String userMessage;

        if (rootMessage.toLowerCase().contains("duplicate entry")) {
            userMessage = "This value already exists, please use another one.";
        } else if (rootMessage.toLowerCase().contains("foreign key")) {
            userMessage = "This record is linked to another resource and cannot be modified.";
        } else if (rootMessage.toLowerCase().contains("cannot be null")) {
            userMessage = "A required field is missing.";
        } else {
            userMessage = "Invalid operation due to database constraints.";
        }

        return buildError(HttpStatus.CONFLICT, userMessage, ex);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String userMessage = "Validation failed, please check your input.";
        return buildError(HttpStatus.BAD_REQUEST, userMessage, ex);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String userMessage = "File size exceeds the maximum allowed limit.";
        return buildError(HttpStatus.PAYLOAD_TOO_LARGE, userMessage, ex);
    }

    @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLocking(Exception ex) {
        String userMessage = "The resource was updated by another process, please try again.";
        return buildError(HttpStatus.CONFLICT, userMessage, ex);
    }
}
