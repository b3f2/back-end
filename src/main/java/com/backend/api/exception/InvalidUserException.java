package com.backend.api.exception;

public class InvalidUserException extends ApiException {

    private static final String MESSAGE = "유효하지 않은 사용자입니다.";

    public InvalidUserException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
