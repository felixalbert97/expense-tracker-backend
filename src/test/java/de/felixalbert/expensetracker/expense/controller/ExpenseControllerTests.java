package de.felixalbert.expensetracker.expense.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Duration;
import java.util.List;

import tools.jackson.databind.ObjectMapper ;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.model.ExpenseTestDataBuilder;
import de.felixalbert.expensetracker.expense.service.ExpenseService;
import de.felixalbert.expensetracker.security.model.CustomUserDetails;
import de.felixalbert.expensetracker.security.service.JwtService;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.model.UserTestDataBuilder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExpenseController.class)
@ActiveProfiles("test")
class ExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private final JwtService jwtService = new JwtService("secret", Duration.ofSeconds(1L));

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllExpenses_returnsExpensesOfAuthenticatedUserAndStatus200() throws Exception {
        // Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        CustomUserDetails principal = new CustomUserDetails(user);

        List<Expense> expenses = ExpenseTestDataBuilder.expensesOf(user);

        when(expenseService.getAll(userId)).thenReturn(expenses);

        // Act & Assert
        mockMvc.perform(
                get("/api/expenses")
                    .with(user(principal))
            )
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expenses)));

        verify(expenseService).getAll(userId);
    }

    @Test
    void createExpense_returnsCreatedExpenseWithAuthenticatedUserAndStatus201() throws Exception {
        // Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        CustomUserDetails principal = new CustomUserDetails(user);

        Expense expense = ExpenseTestDataBuilder.anExpense().withoutUser().build();
        Expense expenseWithUser = ExpenseTestDataBuilder.anExpense().forUser(user).build();
        when(expenseService.create(any(Expense.class), eq(userId)))
            .thenReturn(expenseWithUser);

        // Act & Assert
        mockMvc.perform(
                post("/api/expenses")
                    .with(user(principal))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(expense)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(expenseWithUser)));

        verify(expenseService).create(any(Expense.class), eq(userId));
    }

    @Test
    void updateExpense_success_returnsUpdatedExpenseAndStatus200() throws Exception {
        // Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        CustomUserDetails principal = new CustomUserDetails(user);
        Long id = 42L;
        Expense updated = ExpenseTestDataBuilder.anExpense().forUser(user).withId(id).build();

        when(expenseService.update(eq(id), eq(userId), any(Expense.class))).thenReturn(updated);

        // Act & Assert
        mockMvc.perform(
                put("/api/expenses/42")
                    .with(user(principal))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updated))
            )
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(updated)));
    }

    @Test
    void deleteExpense_success_returns204() throws Exception {
        // Arrange
        Long userId = 1L;
        User user = UserTestDataBuilder.aUser().withId(userId).build();
        CustomUserDetails principal = new CustomUserDetails(user);
        Long id = 42L;

        doNothing().when(expenseService).deleteById(id, userId);

        // Act & Assert
        mockMvc.perform(
                delete("/api/expenses/42")
                    .with(user(principal))
                    .with(csrf())
            )
            .andExpect(status().isNoContent());

        verify(expenseService).deleteById(id, userId);
    }

}