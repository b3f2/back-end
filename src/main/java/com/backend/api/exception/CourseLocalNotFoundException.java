package com.backend.api.exception;

public class CourseLocalNotFoundException extends ApiException{

    private static final String MESSAGE = "CourseId 혹은 LocalId에 해당하는 CourseLocal이 존재하지 않습니다.";

    public CourseLocalNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
