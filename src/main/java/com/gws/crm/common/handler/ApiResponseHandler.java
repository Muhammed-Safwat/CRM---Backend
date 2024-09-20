package com.gws.crm.common.handler;

import com.gws.crm.common.helper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseHandler {

    public static <T> ResponseEntity<ApiResponse<T>> success() {
        return success(null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T body) {
        return success(body, "Success");
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T body, String message) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), message, body, true);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), message, null, true);
        return ResponseEntity.ok(response);
    }


    public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.CREATED.value(), message, null, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> accepted(T body) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.ACCEPTED.value(), "Accepted", body, true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> accepted(String message) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.ACCEPTED.value(), message, null, true);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> badRequest() {
        return error(HttpStatus.BAD_REQUEST, "Bad Request");
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        ApiResponse<Void> response = new ApiResponse<>(status.value(), message, null, false);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Void>> error(String message) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T body) {
        ApiResponse<T> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Resource created successfully", body, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public static ResponseEntity<ApiResponse<Void>> notFound(String message) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, null, false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ApiResponse<Void>> internalServerError() {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ApiResponse<Void>> unauthorized(String message) {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), message, null, false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ApiResponse<Void>> unauthorized() {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null, false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ApiResponse<Void>> forbidden() {
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Forbidden", null, false);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(T body, HttpStatus status, boolean ok, String message) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, body, ok);
        return new ResponseEntity<>(response, status);
    }
}
