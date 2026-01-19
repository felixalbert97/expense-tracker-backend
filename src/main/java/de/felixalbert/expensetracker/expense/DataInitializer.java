package de.felixalbert.expensetracker.expense;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseType;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initExpenses(ExpenseRepository repository) {
        return args -> {
            if (repository.count() == 0){
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
            }
        };
    }
}
