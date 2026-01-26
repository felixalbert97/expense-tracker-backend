package de.felixalbert.expensetracker.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleExpenseNotFound(ExpenseNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Expense not found");
        error.put("id", String.valueOf(ex.getExpenseId()));
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
            .forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleGeneric(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("message", ex.getMessage());
        return error;
    }
}
