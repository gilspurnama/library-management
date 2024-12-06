package com.library.librarymanagement.config;

import com.library.librarymanagement.dto.ResponseDto;
import com.library.librarymanagement.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionMapperConfiguration {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handler(Exception exception) {
        log.error("ExceptionHandler",exception);
        if(exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            return ResponseEntity.badRequest().body(new ResponseDto<>(null, methodArgumentNotValidException.getFieldError().getDefaultMessage()));
        }
        else if(exception instanceof ResponseException){
            ResponseException responseException = (ResponseException) exception;
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(responseException.getHttpStatus());
            if(responseException.getMessage() == null){
                return bodyBuilder.build();
            }
            return bodyBuilder.body(new ResponseDto<>(responseException.getMessage(), responseException.getErrorCode()));
        }
        else if(exception instanceof AccessDeniedException){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ResponseDto<>(exception.getMessage()));
        }
        else{
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseDto<?> responseDto = new ResponseDto<>(exception.getMessage());
            return bodyBuilder.body(responseDto);
        }
    }

}
