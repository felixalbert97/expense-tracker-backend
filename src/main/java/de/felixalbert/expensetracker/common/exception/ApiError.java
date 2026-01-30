package de.felixalbert.expensetracker.common.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, String> fieldErrors
) {
    public static ApiError of(
        HttpStatus status,
        String error,
        String message,
        HttpServletRequest request
    ) {
        return new ApiError(
            Instant.now(),
            status.value(),
            error,
            message,
            request.getRequestURI(),
            null
        );
    }

    public static ApiError validation(
        HttpServletRequest request,
        Map<String, String> fieldErrors
    ) {
        return new ApiError(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Invalid request",
            request.getRequestURI(),
            fieldErrors
        );
    }
}