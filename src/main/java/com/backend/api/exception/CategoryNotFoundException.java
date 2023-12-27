package com.backend.api.exception;

public class CategoryNotFoundException extends ApiException{

    private static final String MESSAGE = "카테고리를 찾을 수 없습니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
