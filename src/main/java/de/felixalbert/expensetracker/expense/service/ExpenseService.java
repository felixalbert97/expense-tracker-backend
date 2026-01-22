package de.felixalbert.expensetracker.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;
import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Expense create(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException(id);
        }
        expenseRepository.deleteById(id);
    }

    public Expense update(Long id, Expense updatedExpense) {
    Expense expense = expenseRepository.findById(id)
        .orElseThrow(() -> new ExpenseNotFoundException(id));

    expense.setAmount(updatedExpense.getAmount());
    expense.setCategory(updatedExpense.getCategory());
    expense.setDate(updatedExpense.getDate());
    expense.setDescription(updatedExpense.getDescription());
    expense.setType(updatedExpense.getType());

    return expenseRepository.save(expense);
}
}