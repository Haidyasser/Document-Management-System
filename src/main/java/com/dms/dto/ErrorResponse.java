package com.dms.dto;



import java.time.LocalDate;
import java.util.Map;


public class ErrorResponse {
    private LocalDate timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    private Map<String, String> validationErrors;

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ErrorResponse() {
        this.timestamp = LocalDate.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;

        this.timestamp = LocalDate.now();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }
}
