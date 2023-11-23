package com.backend.api.exception;

public class InvalidCourseException extends ApiException{

    private static final String MESSAGE = "해당 코스가 없습니다.";


    public InvalidCourseException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
