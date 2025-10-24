package com.dms.exception;

import com.dms.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {

        Map<String, String> validationError = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
           String fieldName = ((FieldError)  error).getField();
           String errorMessage = error.getDefaultMessage();
           validationError.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "validation failed",
                "Input validation failed",
                request.getRequestURI()
        );
        errorResponse.setValidationErrors(validationError);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(BadRequestException e, HttpServletRequest request) {
        Map<String, String> validationError = new HashMap<>();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getMessage(),
                request.getRequestURI()
        );
        errorResponse.setValidationErrors(validationError);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
