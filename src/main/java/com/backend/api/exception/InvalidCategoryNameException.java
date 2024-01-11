package com.backend.api.exception;

public class InvalidCategoryNameException extends ApiException {
    private static final String MESSAGE= "이미 존재하는 카테고리 이름입니다.";

    public InvalidCategoryNameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
