package de.felixalbert.expensetracker.expense.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.testdata.ExpenseTestDataBuilder;

@DataJpaTest
@ActiveProfiles("test")
class ExpenseRepositoryIntegrationTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void saveAndFindExpense_persistsToDatabase() {
        // Arrange
        Expense expense = ExpenseTestDataBuilder.defaultExpense();

        // Act
        Expense saved = expenseRepository.save(expense);
        Expense found = expenseRepository.findById(saved.getId()).orElseThrow();

        // Assert
        assertThat(found.getId()).isNotNull();
        assertThat(found.getAmount()).isEqualTo(expense.getAmount());
        assertThat(found.getCategory()).isEqualTo(expense.getCategory());
        assertThat(found.getDate()).isEqualTo(expense.getDate()); 
        assertThat(found.getDescription()).isEqualTo(expense.getDescription());
        assertThat(found.getType()).isEqualTo(expense.getType());
    }
}