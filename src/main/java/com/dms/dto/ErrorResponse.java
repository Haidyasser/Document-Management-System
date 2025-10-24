package com.dms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ErrorResponse {
    private LocalDate timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;

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

}
