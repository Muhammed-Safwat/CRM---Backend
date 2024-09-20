package com.gws.crm.common.exception;

public class NotFoundResourceException extends RuntimeException {
    public NotFoundResourceException() {
        super("Resource not found !");
    }
}