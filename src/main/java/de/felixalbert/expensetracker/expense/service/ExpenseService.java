package de.felixalbert.expensetracker.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;

import de.felixalbert.expensetracker.expense.exception.ExpenseNotFoundException;
import de.felixalbert.expensetracker.expense.model.Expense;
import de.felixalbert.expensetracker.expense.repository.ExpenseRepository;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.repository.UserRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Expense getById(Long id, Long userId){
        return expenseRepository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public List<Expense> getAll(Long userId) {
        return expenseRepository.findAllByUserId(userId);
    }

    public Expense create(Expense expense, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found"));

        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public void deleteById(Long id, Long userId) {
        Expense existing = getById(id, userId);
        expenseRepository.delete(existing);
    }

    public Expense update(Long id, Long userId, Expense updatedExpense) {
    Expense expense = getById(id, userId);

    expense.setAmount(updatedExpense.getAmount());
    expense.setCategory(updatedExpense.getCategory());
    expense.setDate(updatedExpense.getDate());
    expense.setDescription(updatedExpense.getDescription());
    expense.setType(updatedExpense.getType());

    return expenseRepository.save(expense);
}
}