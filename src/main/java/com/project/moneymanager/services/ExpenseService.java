package com.project.moneymanager.services;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final BalanceService balanceService;

    public ExpenseService(@Lazy ExpenseRepository expenseRepository,@Lazy BalanceService balanceService) {
        this.expenseRepository = expenseRepository;
        this.balanceService = balanceService;
    }

    public Expense addExpense(User u, Expense expense) {
        expense.setUser(u);
        expenseRepository.save(expense);
        balanceService.transaction(u);
        return expense;
    }

    public void updateExpense(Long id, Expense expense) {
        Expense newExpense = findExpenseById(id);
        newExpense.setDescription(expense.getDescription());
        newExpense.setAmount(expense.getAmount());
        expenseRepository.save(newExpense);
        balanceService.transaction(newExpense.getUser());
    }

    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public void deleteExpense(User user, Long id) {
        expenseRepository.deleteById(id);
        balanceService.transaction(user);
    }

    public List<Expense> findAllExpenses() {
        return (List<Expense>) expenseRepository.findAll();
    }
}
