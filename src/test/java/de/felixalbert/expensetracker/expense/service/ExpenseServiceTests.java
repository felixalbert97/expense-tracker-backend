package de.felixalbert.expensetracker.expense.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;
import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseTestDataBuilder;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;
import de.felixalbert.expensetracker.user.exception.UserNotFoundException;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.model.UserTestDataBuilder;
import de.felixalbert.expensetracker.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ExpenseServiceTests {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    // ---------- getById ----------

    @Test 
    void getById_existingExpenseOfUser_returnsExpenseOfUser() {
        //Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        Long id = 42L;
        Expense expense = ExpenseTestDataBuilder.anExpense().withId(id).forUser(user).build();
        when(expenseRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(expense));

        //Act
        Expense result = expenseService.getById(id, userId);

        //Assert
        assertThat(result).isEqualTo(expense);
        verify(expenseRepository).findByIdAndUserId(id, userId);
    }

    @Test 
    void getById_nonExistingExpenseOfUser_throwsException() {
        //Arrange
        Long userId = 999L;
        Long Id = 999L;
        when(expenseRepository.findByIdAndUserId(Id, userId)).thenReturn(Optional.empty());

        // Act & Assert
        ExpenseNotFoundException ex = assertThrows(
            ExpenseNotFoundException.class,
            () -> expenseService.getById(Id, userId)
        );
        assertThat(ex.getExpenseId()).isEqualTo(Id);
        verify(expenseRepository, never()).save(any());
    }

    // ---------- getAll ----------

    @Test
    void getAll_returnsAllExpensesOfUser() {
        //Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        List<Expense> expenses = ExpenseTestDataBuilder.expensesOf(user);
        when(expenseRepository.findAllByUserId(userId)).thenReturn(expenses);

        //Act
        List<Expense> result = expenseService.getAll(userId);

        //Assert
        assertThat(result).isEqualTo(expenses);
        verify(expenseRepository).findAllByUserId(userId);
    }

    // ---------- create ----------

    @Test
    void create_existingUser_assignsUserAndSavesExpense() {
        // Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        Expense expense = ExpenseTestDataBuilder.defaultExpense();

        when(userRepository.findById(userId))
            .thenReturn(Optional.of(user));

        when(expenseRepository.save(any(Expense.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Expense result = expenseService.create(expense, userId);
        
        // Assert
        assertThat(result.getUser()).isEqualTo(user);
        verify(userRepository).findById(userId);
        verify(expenseRepository).save(expense);
    }

    @Test
    void create_nonExistingUser_throwsException() {
        // Arrange
        Long userId = 999L;
        Expense expense = ExpenseTestDataBuilder.defaultExpense();

        when(userRepository.findById(userId))
            .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException ex = assertThrows(
            UserNotFoundException.class,
            () -> expenseService.create(expense, userId)
        );

        assertThat(ex.getUserId()).isEqualTo(userId);
        verify(expenseRepository, never()).save(any());
    }

    // ---------- deleteById ----------
 
    @Test
    void deleteById_existingExpenseOfUser_deletesExpense() {
        //Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        Long id = 42L;
        Expense expense = ExpenseTestDataBuilder.anExpense().withId(id).forUser(user).build();
        when(expenseRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(expense));

        //Act 
        expenseService.deleteById(id, userId);

        //Assert
        verify(expenseRepository).findByIdAndUserId(id, userId);
        verify(expenseRepository).delete(expense);
    }

    // ---------- update ----------

    @Test
    void update_existingExpenseOfUser_updatesAndReturnsExpenseAndDoesNotChangeUser() {
        //Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        Long id = 42L;
        Expense existing = ExpenseTestDataBuilder.anExpense().withId(id).forUser(user).build();
        Expense updated = ExpenseTestDataBuilder.anExpense().withId(id).forUser(user).withCategory("UPDATED").build();

        when(expenseRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(existing));
        when(expenseRepository.save(existing)).thenReturn(existing);

        //Act
        Expense result = expenseService.update(id, userId, updated);

        //Assert
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCategory()).isEqualTo(updated.getCategory());
        verify(expenseRepository).findByIdAndUserId(id, userId);
        verify(expenseRepository).save(existing);
    }

}