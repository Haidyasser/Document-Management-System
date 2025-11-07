package com.dms.exception;

public class FileUploadExceptionAdvice extends RuntimeException {
    public FileUploadExceptionAdvice(String message) {
        super(message);
    }
}
