package org.example.cardservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorMessage {
    private String message;
    private HttpStatus httpStatus;

    public ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
