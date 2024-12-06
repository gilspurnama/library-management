package com.library.librarymanagement.util;

import org.springframework.http.HttpStatus;

public enum ExceptionEnum {
    SUCCESS                                                 (200, HttpStatus.OK, null),
    EMAIL_ALREADY_EXIST                                     (40001, HttpStatus.BAD_REQUEST, "Email already exists"),
    EMAIL_WRONG_FORMAT                                      (40002, HttpStatus.BAD_REQUEST, "Wrong email format"),
    PASSWORD_WRONG_FORMAT                                   (40003, HttpStatus.BAD_REQUEST, "Wrong password format. Must be 8 character with at least 1 capital letter and no special character"),
    WRONG_CREDENTIAL                                        (40004, HttpStatus.BAD_REQUEST, "Wrong credentials"),
    UNAUTHORIZED                                            (40005, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    USER_NOT_FOUND                                          (40006, HttpStatus.NOT_FOUND, "User Not Found"),
    USER_BORROW_LIMIT                                       (40007, HttpStatus.BAD_REQUEST, "User has reach borrowing book limit"),
    BOOK_ALREADY_EXIST                                      (50001, HttpStatus.BAD_REQUEST, "Book already exists"),
    BOOK_NOT_FOUND                                          (50002, HttpStatus.NOT_FOUND, "Book Not Found"),
    BOOK_INSUFFICIENT                                       (50002, HttpStatus.NOT_FOUND, "No more book in the inventory"),
    LOAN_NOT_FOUND                                          (60001, HttpStatus.NOT_FOUND, "User has not loan the book");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ExceptionEnum(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    public String message() {
        return this.message;
    }

}
