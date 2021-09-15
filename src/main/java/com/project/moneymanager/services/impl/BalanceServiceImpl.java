package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Balance;
import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.BalanceRepository;
import com.project.moneymanager.services.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final BalanceRepository balanceRepository;
    private final IncomeServiceImpl incomeServiceImpl;
    private final ExpenseServiceImpl expenseService;

    @Autowired
    public BalanceServiceImpl(@Lazy BalanceRepository balanceRepository, @Lazy IncomeServiceImpl incomeServiceImpl, @Lazy ExpenseServiceImpl expenseService) {
        this.balanceRepository = balanceRepository;
        this.incomeServiceImpl = incomeServiceImpl;
        this.expenseService = expenseService;
    }

    public Balance getLastBalance(User user) {
        if (user != null) {
            LOGGER_INFO.info("Start getting the last balance from the user: " + user.getUsername());
            Balance balance = null;
            for (Balance b : user.getBalances()) {
                balance = b;
            }
            if (balance == null) {
                balance = new Balance(user, 0);
                balanceRepository.save(balance);
            }
            LOGGER_INFO.info("End getting the last balance from the user: " + user.getUsername());
            return balance;
        } else {
            LOGGER_ERROR.error("User doesn't exists to get the last balance!");
            return null;
        }
    }

    public void transaction(User user) {
        if (user != null) {
            LOGGER_INFO.info("Start transaction with balance from user " + user.getId());
            int total = 0;
            for (Income i : incomeServiceImpl.findAllIncomes().stream()
                    .filter(income -> income.getUser().equals(user))
                    .collect(Collectors.toList())) {
                total += i.getAmount();
            }
            for (Expense i : expenseService.findAllExpenses().stream()
                    .filter(income -> income.getUser().equals(user))
                    .collect(Collectors.toList())) {
                total -= i.getAmount();
            }
            Balance bal = new Balance(user, total);
            balanceRepository.save(bal);
            LOGGER_INFO.info("Start transaction with balance from user " + user.getId());
        } else {
            LOGGER_ERROR.error("User doesn't exists to do the transaction!");
        }
    }
}
