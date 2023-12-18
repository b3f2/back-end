package com.backend.api.controller;

import com.backend.api.exception.*;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CourseNotFoundException.class)
    public ApiResponse<Object> handleInvalidCourseException(CourseNotFoundException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LocalNotFoundException.class)
    public ApiResponse<Object> handleLocalNotFoundException(LocalNotFoundException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LocalReviewNotFoundException.class)
    public ApiResponse<Object> handleLocalReviewNotFoundException(LocalReviewNotFoundException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FavoriteNotFoundException.class)
    public ApiResponse<Object> handleFavoriteNotFoundException(FavoriteNotFoundException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }
}
