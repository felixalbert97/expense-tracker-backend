package de.felixalbert.expensetracker.expense.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import de.felixalbert.expensetracker.user.model.User;

public class ExpenseTestDataBuilder {

    // ---------- Default values ----------
    private Long id;
    private BigDecimal amount = BigDecimal.valueOf(10.00);
    private String category = "Food";
    private LocalDate date = LocalDate.of(2026, 1, 1);
    private String description = "Test expense";
    private ExpenseType type = ExpenseType.EXPENSE;

    private User user; 

    private ExpenseTestDataBuilder() {
        // force usage via factory method
    }

    // ---------- Factory method ----------
    public static ExpenseTestDataBuilder anExpense() {
        return new ExpenseTestDataBuilder();
    }

    // ---------- Fluent modifiers ----------
    public ExpenseTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ExpenseTestDataBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseTestDataBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public ExpenseTestDataBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public ExpenseTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ExpenseTestDataBuilder withType(ExpenseType type) {
        this.type = type;
        return this;
    }

    // ---------- User ownership ----------
    public ExpenseTestDataBuilder forUser(User user) {
        this.user = user;
        return this;
    }

    public ExpenseTestDataBuilder withoutUser() {
        this.user = null;
        return this;
    }

    // ---------- Build ----------
    public Expense build() {
        Expense expense = new Expense(
            id,
            amount,
            category,
            date,
            description,
            type
        );

        if (user != null) {
            expense.setUser(user);
        }

        return expense;
    }

    // ---------- Convenience methods ----------
    public static Expense defaultExpense() {
        return anExpense().build();
    }

    public static Expense expenseOf(User user) {
        return anExpense()
            .forUser(user)
            .build();
    }

    public static List<Expense> expensesOf(User user) {
        return List.of(
            anExpense()
                .forUser(user)
                .withAmount(BigDecimal.valueOf(12.50))
                .withDescription("Lunch")
                .build(),

            anExpense()
                .forUser(user)
                .withAmount(BigDecimal.valueOf(50.00))
                .withCategory("Transport")
                .withDescription("Train ticket")
                .build(),

            anExpense()
                .forUser(user)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCategory("Rent")
                .withDescription("Monthly rent")
                .build()
        );
    }
}