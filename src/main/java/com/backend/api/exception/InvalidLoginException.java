package com.backend.api.exception;

public class InvalidLoginException extends ApiException {

    private static final String MESSAGE = "아이디 혹은 비밀번호가 일치하지 않습니다.";

    public InvalidLoginException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
