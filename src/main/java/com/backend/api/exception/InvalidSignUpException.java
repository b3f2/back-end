package com.backend.api.exception;

public class InvalidSignUpException extends ApiException {

    private static final String MESSAGE = "이미 가입되어 있는 이메일 입니다.";

    public InvalidSignUpException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }

}
