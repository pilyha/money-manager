package com.project.moneymanager.services;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.User;

import java.util.List;

public interface ExpenseService {

    void addExpense(User user, Expense expense);

    void updateExpense(Long id, Expense expense);

    Expense findExpenseById(Long id);

    void deleteExpense(User user, Long id);

    List<Expense> findAllExpenses();
}
