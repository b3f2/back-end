package com.backend.api.exception;

public class NotYourPostException extends ApiException {
    private static final String MESSAGE = "회원님이 작성한 게시물이 아닙니다.";

    public NotYourPostException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
