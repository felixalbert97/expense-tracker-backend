package de.felixalbert.expensetracker.expense.testdata;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseType;

public class ExpenseTestDataBuilder {

    // Default values
    private BigDecimal amount = BigDecimal.valueOf(10.00);
    private String category = "Food";
    private LocalDate date = LocalDate.of(2026, 1, 1);
    private String description = "Test expense";
    private ExpenseType type = ExpenseType.EXPENSE;

    private ExpenseTestDataBuilder() {
        // force usage via factory method
    }

    // ---------- Factory method ----------
    public static ExpenseTestDataBuilder anExpense() {
        return new ExpenseTestDataBuilder();
    }

    // ---------- Fluent modifiers ----------
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

    // ---------- Build ----------
    public Expense build() {
        return new Expense(
                amount,
                category,
                date,
                description,
                type
        );
    }

    // ---------- Convenience methods ----------
    public static Expense defaultExpense() {
        return anExpense().build();
    }

    public static List<Expense> defaultExpenses() {
        return List.of(
                anExpense()
                        .withAmount(BigDecimal.valueOf(12.50))
                        .withDescription("Lunch")
                        .build(),

                anExpense()
                        .withAmount(BigDecimal.valueOf(50.00))
                        .withCategory("Transport")
                        .withDescription("Train ticket")
                        .build(),

                anExpense()
                        .withAmount(BigDecimal.valueOf(100.00))
                        .withCategory("Rent")
                        .withDescription("Monthly rent")
                        .build()
        );
    }
}