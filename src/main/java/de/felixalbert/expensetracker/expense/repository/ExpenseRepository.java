package de.felixalbert.expensetracker.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.felixalbert.expensetracker.expense.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}