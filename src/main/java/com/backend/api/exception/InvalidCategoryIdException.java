package com.backend.api.exception;

public class InvalidCategoryIdException extends ApiException {
    private static final String MESSAGE= "이미 존재하는 카테고리 Id입니다.";

    public InvalidCategoryIdException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
