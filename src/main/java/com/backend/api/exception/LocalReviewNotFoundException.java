package com.backend.api.exception;

public class LocalReviewNotFoundException extends ApiException{


    private static final String MESSAGE = "해당 장소 리뷰를 찾을 수 없습니다.";

    public LocalReviewNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
