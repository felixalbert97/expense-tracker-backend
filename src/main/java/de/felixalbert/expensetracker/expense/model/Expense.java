package de.felixalbert.expensetracker.expense.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotNull(message = "Date must not be null")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Expense type must not be null")
    private ExpenseType type;

    protected Expense() {
        // JPA braucht einen parameterlosen Konstruktor
    }

    public Expense(BigDecimal amount, String category, LocalDate date, String description, ExpenseType type) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    // Getter 
    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseType getType() {
        return type;
    }

    // Setter 
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }
}
