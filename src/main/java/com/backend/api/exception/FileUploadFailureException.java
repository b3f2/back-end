package com.backend.api.exception;

public class FileUploadFailureException extends ApiException {
    public FileUploadFailureException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
