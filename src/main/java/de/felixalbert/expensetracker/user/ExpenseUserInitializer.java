package de.felixalbert.expensetracker.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseType;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;
import de.felixalbert.expensetracker.security.repository.RefreshTokenRepository;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.repository.UserRepository;
import de.felixalbert.expensetracker.user.service.UserService;

@Configuration
@Profile("local")
public class ExpenseUserInitializer {

    @Bean
    CommandLineRunner initTestUserWithExpenses(
        UserService userService,
        UserRepository userRepository,
        ExpenseRepository expenseRepository,
        RefreshTokenRepository refreshTokenRepository
        ) { 
        return args -> {
            refreshTokenRepository.deleteAll();
            expenseRepository.deleteAll();
            userRepository.deleteAll();
            User testUser1 = userService.createUser("test@test.de", "password123");
            User testUser2 = userService.createUser("test2@example.com", "password123");

            Expense testExpense1 = new Expense(
                        new BigDecimal("50.00"),
                        "Groceries",
                        LocalDate.now(),
                        "Supermarket",
                        ExpenseType.EXPENSE
                );
            testExpense1.setUser(testUser1);
            expenseRepository.save(testExpense1);

            Expense testExpense2 = new Expense(
                        new BigDecimal("3000.00"),
                        "Salary",
                        LocalDate.now(),
                        "Monthly salary",
                        ExpenseType.INCOME
                );
            testExpense2.setUser(testUser1);
            expenseRepository.save(testExpense2);


            Expense testExpense3 = new Expense(
                        new BigDecimal("30.00"),
                        "Groceries",
                        LocalDate.now(),
                        "Supermarket",
                        ExpenseType.EXPENSE
                );
            testExpense3.setUser(testUser2);
            expenseRepository.save(testExpense3);

            Expense testExpense4 = new Expense(
                        new BigDecimal("2000.00"),
                        "Salary",
                        LocalDate.now(),
                        "Monthly salary",
                        ExpenseType.INCOME
                );
            testExpense4.setUser(testUser2);
            expenseRepository.save(testExpense4);
        };
    }

}