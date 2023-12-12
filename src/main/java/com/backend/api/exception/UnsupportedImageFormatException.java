package com.backend.api.exception;

public class UnsupportedImageFormatException extends ApiException {
    private static final String MESSAGE = "지원하지 않는 형식의 이미지 파일입니다.";

    public UnsupportedImageFormatException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
