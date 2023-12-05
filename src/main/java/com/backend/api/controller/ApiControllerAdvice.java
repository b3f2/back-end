package com.backend.api.controller;

import com.backend.api.exception.InvalidCourseException;
import com.backend.api.exception.InvalidUserException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidCourseException.class)
    public ApiResponse<Object> handleInvalidCourseException(InvalidCourseException e) {
        return ApiResponse.of(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidUserException.class)
    public ApiResponse<Object> handleInvalidUserException(InvalidUserException e) {
        return ApiResponse.of(
                HttpStatus.UNAUTHORIZED,
                e.getMessage(),
                null
        );
    }
}
