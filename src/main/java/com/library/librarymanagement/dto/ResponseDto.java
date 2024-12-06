package com.library.librarymanagement.dto;

public class ResponseDto<T> {
    public T payload;
    public String message;
    public Integer errorCode;

    public ResponseDto(T payload) {
        this(payload,null);
    }

    public ResponseDto(T payload, String message) {
        this.payload = payload;
        this.message = message;
    }

    public ResponseDto(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
