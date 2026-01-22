package de.felixalbert.expensetracker.expense.exception;

public class ExpenseNotFoundException extends RuntimeException {

    private final Long expenseId;

    public ExpenseNotFoundException(Long expenseId) {
        super("Expense not found with id: " + expenseId);
        this.expenseId = expenseId;
    }

    public Long getExpenseId() {
        return expenseId;
    }
}