package com.backend.api.exception;

public class InvalidTokenException extends ApiException {

    private static final String MESSAGE = "유효하지않은 토큰 입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
