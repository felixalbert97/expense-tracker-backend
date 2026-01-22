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
        List<Expense> expenses = ExpenseTestDataBuilder.defaultExpenses();
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.findAll();

        assertThat(result).isEqualTo(expenses);
        verify(expenseRepository).findAll();
    }

    // ---------- create ----------

    @Test
    void create_savesAndReturnsExpense() {
        Expense expense = ExpenseTestDataBuilder.defaultExpense();
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense result = expenseService.create(expense);

        assertThat(result).isEqualTo(expense);
        verify(expenseRepository).save(expense);
    }

    // ---------- deleteById ----------

    @Test
    void deleteById_existingId_deletesExpense() {
        Long id = 1L;
        when(expenseRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> expenseService.deleteById(id));
        verify(expenseRepository).existsById(id);
        verify(expenseRepository).deleteById(id);
    }

    @Test
    void deleteById_nonExistingId_throwsException() {
        Long id = 999L;
        when(expenseRepository.existsById(id)).thenReturn(false);

        ExpenseNotFoundException ex = assertThrows(
                ExpenseNotFoundException.class,
                () -> expenseService.deleteById(id)
        );

        assertEquals(id, ex.getExpenseId());
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    // ---------- update ----------

    @Test
    void update_existingExpense_updatesAndReturnsExpense() {
        Long id = 1L;
        Expense existing = ExpenseTestDataBuilder.defaultExpense();
        Expense updated = ExpenseTestDataBuilder.anExpense()
                .withCategory("Updated")
                .build();

        when(expenseRepository.findById(id)).thenReturn(Optional.of(existing));
        when(expenseRepository.save(existing)).thenReturn(existing);

        Expense result = expenseService.update(id, updated);

        assertThat(result.getCategory()).isEqualTo(updated.getCategory());
        verify(expenseRepository).findById(id);
        verify(expenseRepository).save(existing);
    }

    @Test
    void update_nonExistingExpense_throwsException() {
        Long id = 99L;
        Expense updated = ExpenseTestDataBuilder.defaultExpense();

        when(expenseRepository.findById(id)).thenReturn(Optional.empty());

        ExpenseNotFoundException ex = assertThrows(
                ExpenseNotFoundException.class,
                () -> expenseService.update(id, updated)
        );

        assertEquals(id, ex.getExpenseId());
        verify(expenseRepository).findById(id);
        verify(expenseRepository, never()).save(any());
    }
}