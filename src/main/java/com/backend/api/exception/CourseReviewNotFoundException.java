package com.backend.api.exception;

public class CourseReviewNotFoundException extends ApiException{

    private static final String MESSAGE = "해당 코스 리뷰가 없습니다.";

    public CourseReviewNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
