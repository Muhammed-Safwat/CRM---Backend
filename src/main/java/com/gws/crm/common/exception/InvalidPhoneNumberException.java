package com.gws.crm.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException() {
        super("Phone Number not valid");
    }
}
