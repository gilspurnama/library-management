package com.library.librarymanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseException extends RuntimeException {
    private final Integer errorCode;
    private final HttpStatus httpStatus;

    public ResponseException(String message, Integer errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = status;
    }
}
