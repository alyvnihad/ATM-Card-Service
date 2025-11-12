package org.example.cardservice.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ControllerException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
