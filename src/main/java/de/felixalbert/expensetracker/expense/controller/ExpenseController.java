package de.felixalbert.expensetracker.expense.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.service.ExpenseService;
import de.felixalbert.expensetracker.security.model.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(expenseService.getAll(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense, @AuthenticationPrincipal CustomUserDetails user) {
        Expense created = expenseService.create(expense, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense updatedExpense,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(expenseService.update(id, user.getId(), updatedExpense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        expenseService.deleteById(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}