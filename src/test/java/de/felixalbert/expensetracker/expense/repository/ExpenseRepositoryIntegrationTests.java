package de.felixalbert.expensetracker.expense.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseTestDataBuilder;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.model.UserTestDataBuilder;
import de.felixalbert.expensetracker.user.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
class ExpenseRepositoryIntegrationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByUserId_returnsOnlyExpensesOfThatUser() {
        // Arrange
        User user1 = userRepository.save(
            UserTestDataBuilder.aUser().build()
        );
        User user2 = userRepository.save(
            UserTestDataBuilder.anotherUser().build()
        );

        expenseRepository.saveAll(
            ExpenseTestDataBuilder.expensesOf(user1)
        );
        expenseRepository.saveAll(
            ExpenseTestDataBuilder.expensesOf(user2)
        );

        // Act
        List<Expense> result = expenseRepository.findAllByUserId(user1.getId());

        // Assert
        assertThat(result)
            .allMatch(e -> e.getUser().getId().equals(user1.getId()))
            .hasSize(ExpenseTestDataBuilder.expensesOf(user1).size());
    }

    @Test
    void findByIdAndUserId_returnsExpenseOnlyIfOwnedByUser() {
        // Arrange
        User user = userRepository.save(
            UserTestDataBuilder.aUser().build()
        );

        Expense expense = expenseRepository.save(
            ExpenseTestDataBuilder.anExpense().forUser(user).build()
        );

        // Act
        Optional<Expense> found =
            expenseRepository.findByIdAndUserId(expense.getId(), user.getId());

        Optional<Expense> notFound =
            expenseRepository.findByIdAndUserId(expense.getId(), 999L);

        // Assert
        assertThat(found).isPresent();
        assertThat(notFound).isEmpty();
    }
}