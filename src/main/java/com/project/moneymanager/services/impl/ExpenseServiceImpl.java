package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Plan;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.ExpenseRepository;
import com.project.moneymanager.services.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final ExpenseRepository expenseRepository;
    private final BalanceServiceImpl balanceServiceImpl;
    private final PlanServiceImpl planService;

    @Autowired
    public ExpenseServiceImpl(@Lazy ExpenseRepository expenseRepository, @Lazy BalanceServiceImpl balanceServiceImpl, @Lazy PlanServiceImpl planService) {
        this.expenseRepository = expenseRepository;
        this.balanceServiceImpl = balanceServiceImpl;

        this.planService = planService;
    }

    public void addExpense(User user, Expense expense) {
        if (expense != null) {
            LOGGER_INFO.info("Start create expense: " + expense.getDescription());
            int totalExpense = 0;
            for (Plan plan : planService.findAllPlans().stream().filter(plan -> plan.getUser().getId().equals(user.getId())).collect(Collectors.toList())) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
                String today = df.format(Calendar.getInstance().getTime());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(plan.getStart_date());
                if (today.compareTo(strDate) > 0 && df.format(expense.getDate()).compareTo(strDate) > 0) {
                    for (Expense expens : findAllExpenses().stream().filter(expense1 -> expense1.getPlan().getId().equals(plan.getId())).collect(Collectors.toList())) {
                        totalExpense = +expens.getAmount();
                    }
                    if (totalExpense + expense.getAmount() > plan.getLimitz()) {
                        throw new RuntimeException("Expense have exceeded plan");

                    } else {
                        expense.setPlan(plan);
                    }
                }
            }
            if (expense.getPlan() == null) {
                throw new NullPointerException("Expense hasn't plan");
            }
            expense.setUser(user);
            expenseRepository.save(expense);
            balanceServiceImpl.transaction(user);
            LOGGER_INFO.info("End create expense: " + expense.getDescription());
        } else {
            LOGGER_ERROR.error("Expense is null!");
        }
    }

    public void updateExpense(Long id, Expense expense) {
        if (expenseRepository.existsById(id)) {
            LOGGER_WARN.warn("Start update expense: " + id);
            Expense newExpense = findExpenseById(id);
            newExpense.setDescription(expense.getDescription());
            newExpense.setAmount(expense.getAmount());
            expenseRepository.save(newExpense);
            balanceServiceImpl.transaction(newExpense.getUser());
            LOGGER_WARN.warn("End update expense: " + id);
        } else {
            LOGGER_ERROR.error("Expense doesn't exists");
        }
    }

    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public void deleteExpense(User user, Long id) {
        if (expenseRepository.existsById(id)) {
            LOGGER_WARN.warn("Start delete expense: " + id);
            expenseRepository.deleteById(id);
            balanceServiceImpl.transaction(user);
            LOGGER_WARN.warn("End delete expense: " + id);
        } else {
            LOGGER_ERROR.error("Expense doesn't exists");
        }
    }

    public List<Expense> findAllExpenses() {
        LOGGER_INFO.info("Read all expenses");
        return (List<Expense>) expenseRepository.findAll();
    }
}
