package de.felixalbert.expense_tracker;

import de.felixalbert.expense_tracker.expense.Expense;
import de.felixalbert.expense_tracker.expense.ExpenseRepository;
import de.felixalbert.expense_tracker.expense.ExpenseType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initExpenses(ExpenseRepository repository) {
        return args -> {
            repository.save(new Expense(
                    new BigDecimal("50.00"),
                    "Groceries",
                    LocalDate.now(),
                    "Supermarket",
                    ExpenseType.EXPENSE
            ));

            repository.save(new Expense(
                    new BigDecimal("2000.00"),
                    "Salary",
                    LocalDate.now(),
                    "Monthly salary",
                    ExpenseType.INCOME
            ));
        };
    }
}
