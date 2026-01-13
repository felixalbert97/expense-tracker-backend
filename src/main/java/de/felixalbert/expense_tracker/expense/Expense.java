package de.felixalbert.expense_tracker.expense;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String category;

    private LocalDate date;

    private String description;

    @Enumerated(EnumType.STRING)
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
