package de.felixalbert.expensetracker.expense.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import tools.jackson.databind.ObjectMapper ;

import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.service.ExpenseService;
import de.felixalbert.expensetracker.expense.testdata.ExpenseTestDataBuilder;
import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllExpenses_returnsListAndStatus200() throws Exception {
        // Arrange
        List<Expense> expenses = ExpenseTestDataBuilder.defaultExpenses();
        when(expenseService.findAll()).thenReturn(expenses);

        // Act & Assert
        mockMvc.perform(get("/api/expenses"))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expenses)));
    }

    @Test
    void createExpense_returnsCreatedExpenseAndStatus201() throws Exception {
        // Arrange
        Expense expense = ExpenseTestDataBuilder.defaultExpense();
        when(expenseService.create(any(Expense.class))).thenReturn(expense);

        // Act & Assert
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
               .andExpect(status().isCreated())
               .andExpect(content().json(objectMapper.writeValueAsString(expense)));
    }

    @Test
    void deleteExpense_success_returns204() throws Exception {
        // Arrange
        doNothing().when(expenseService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/expenses/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void deleteExpense_notFound_returns404() throws Exception {
        // Arrange
        doThrow(new ExpenseNotFoundException(999L)).when(expenseService).deleteById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/expenses/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateExpense_success_returnsUpdatedExpense() throws Exception {
        // Arrange
        Long id = 1L;
        Expense updated = ExpenseTestDataBuilder.defaultExpense();

        when(expenseService.update(eq(id), any(Expense.class))).thenReturn(updated);

        // Act & Assert
        mockMvc.perform(put("/api/expenses/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(updated)));
    }

    @Test
    void updateExpense_notFound_returns404() throws Exception {
        // Arrange
        Long id = 999L;
        Expense updated = ExpenseTestDataBuilder.defaultExpense();
        doThrow(new ExpenseNotFoundException(id)).when(expenseService).update(eq(id), any(Expense.class));

        // Act & Assert
        mockMvc.perform(put("/api/expenses/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
               .andExpect(status().isNotFound());
    }
}