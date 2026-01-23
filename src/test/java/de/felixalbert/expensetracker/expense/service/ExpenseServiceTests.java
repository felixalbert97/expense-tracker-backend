package de.felixalbert.expensetracker.expense.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;
import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;
import de.felixalbert.expensetracker.expense.testdata.ExpenseTestDataBuilder;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTests {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    // ---------- findAll ----------

    @Test
    void findAll_returnsAllExpenses() {
        //Arrange
        List<Expense> expenses = ExpenseTestDataBuilder.defaultExpenses();
        when(expenseRepository.findAll()).thenReturn(expenses);

        //Act
        List<Expense> result = expenseService.findAll();

        //Assert
        assertThat(result).isEqualTo(expenses);
        verify(expenseRepository).findAll();
    }

    // ---------- create ----------

    @Test
    void create_savesAndReturnsExpense() {
        //Arrange
        Expense expense = ExpenseTestDataBuilder.defaultExpense();
        when(expenseRepository.save(expense)).thenReturn(expense);
        
        //Act
        Expense result = expenseService.create(expense);

        //Assert
        assertThat(result).isEqualTo(expense);
        verify(expenseRepository).save(expense);
    }

    // ---------- deleteById ----------

    @Test
    void deleteById_existingId_deletesExpense() {
        //Arrange
        Long id = 1L;
        when(expenseRepository.existsById(id)).thenReturn(true);

        //Act & Assert
        assertDoesNotThrow(() -> expenseService.deleteById(id));
        verify(expenseRepository).existsById(id);
        verify(expenseRepository).deleteById(id);
    }

    @Test
    void deleteById_nonExistingId_throwsException() {
        //Arrange
        Long id = 999L;
        when(expenseRepository.existsById(id)).thenReturn(false);

        //Act
        ExpenseNotFoundException ex = assertThrows(
                ExpenseNotFoundException.class,
                () -> expenseService.deleteById(id)
        );

        //Assert
        assertEquals(id, ex.getExpenseId());
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    // ---------- update ----------

    @Test
    void update_existingExpense_updatesAndReturnsExpense() {
        //Arrange
        Long id = 1L;
        Expense existing = ExpenseTestDataBuilder.defaultExpense();
        Expense updated = ExpenseTestDataBuilder.anExpense()
                .withCategory("Updated")
                .build();
        when(expenseRepository.findById(id)).thenReturn(Optional.of(existing));
        when(expenseRepository.save(existing)).thenReturn(existing);

        //Act
        Expense result = expenseService.update(id, updated);

        //Assert
        assertThat(result.getCategory()).isEqualTo(updated.getCategory());
        verify(expenseRepository).findById(id);
        verify(expenseRepository).save(existing);
    }

    @Test
    void update_nonExistingExpense_throwsException() {
        //Arrange
        Long id = 99L;
        Expense updated = ExpenseTestDataBuilder.defaultExpense();
        when(expenseRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        ExpenseNotFoundException ex = assertThrows(
                ExpenseNotFoundException.class,
                () -> expenseService.update(id, updated)
        );
        assertEquals(id, ex.getExpenseId());
        verify(expenseRepository).findById(id);
        verify(expenseRepository, never()).save(any());
    }
}