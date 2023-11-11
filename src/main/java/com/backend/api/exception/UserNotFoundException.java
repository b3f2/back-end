package com.backend.api.exception;

public class UserNotFoundException extends ApiException {

    private static final String MESSAGE = "가입된 회원을 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
