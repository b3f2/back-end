package com.backend.api.exception;

public class LocalNotFoundException extends ApiException{

    private static final String MESSAGE = "해당 지역이 없습니다.";

    public LocalNotFoundException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
