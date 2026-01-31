package de.felixalbert.expensetracker.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;
import de.felixalbert.expensetracker.security.exception.InvalidRefreshTokenException;
import de.felixalbert.expensetracker.security.exception.RefreshTokenExpiredException;
import de.felixalbert.expensetracker.user.exception.UserAlreadyInUseException;
import de.felixalbert.expensetracker.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyInUseException.class)
    public ResponseEntity<ApiError> handleUserAlreadyInUse(
        UserAlreadyInUseException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiError.of(
                HttpStatus.CONFLICT,
                "USER_ALREADY_IN_USE_EXCEPTION",
                ex.getMessage(),
                request
            ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
        BadCredentialsException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiError.of(
                HttpStatus.UNAUTHORIZED,
                "BAD_CREDENTIALS",
                "Invalid email or password",
                request
            ));
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiError> handleExpenseNotFound(
        ExpenseNotFoundException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiError.of(
                HttpStatus.NOT_FOUND,
                "EXPENSE_NOT_FOUND",
                ex.getMessage(),
                request
            ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
        UserNotFoundException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiError.of(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                ex.getMessage(),
                request
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
            );

        return ResponseEntity.badRequest()
            .body(ApiError.validation(request, fieldErrors));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiError> handleInvalidRefresh(
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiError.of(
                HttpStatus.UNAUTHORIZED,
                "INVALID_REFRESH_TOKEN",
                "Invalid refresh token",
                request
            ));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiError> handleExpiredRefresh(
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiError.of(
                HttpStatus.UNAUTHORIZED,
                "REFRESH_EXPIRED",
                "Session expired. Please log in again.",
                request
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
        Exception ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Internal server error",
                request
            ));
    }
}
