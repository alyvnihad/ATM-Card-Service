package org.example.cardservice.exception;

import org.springframework.http.HttpStatus;

public abstract class ControllerException extends RuntimeException {
    private final HttpStatus httpStatus;
    public ControllerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
