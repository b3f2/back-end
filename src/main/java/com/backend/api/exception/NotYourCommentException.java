package com.backend.api.exception;

public class NotYourCommentException extends ApiException {
    private static final String MESSAGE = "회원님이 작성한 댓글이 아닙니다.";

    public NotYourCommentException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
