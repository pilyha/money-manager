package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.IncomeRepository;
import com.project.moneymanager.services.IncomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final IncomeRepository incomeRepository;
    private final BalanceServiceImpl balanceServiceImpl;

    @Autowired
    public IncomeServiceImpl(@Lazy IncomeRepository incomeRepository, @Lazy BalanceServiceImpl balanceServiceImpl) {
        this.incomeRepository = incomeRepository;
        this.balanceServiceImpl = balanceServiceImpl;
    }

    public void addIncome(User user, Income income) {
        if (income != null) {
            LOGGER_INFO.info("Start create income: " + income.getDescription());
            income.setUser(user);
            incomeRepository.save(income);
            balanceServiceImpl.transaction(user);
            LOGGER_INFO.info("End create income: " + income.getDescription());
        } else {
            LOGGER_ERROR.error("Expense is null!");
        }
    }

    public void updateIncome(Long id, Income income) {
        if (incomeRepository.existsById(id)) {
            LOGGER_WARN.warn("Start update income: " + id);
            Income newIncome = findIncomeById(id);
            newIncome.setDescription(income.getDescription());
            newIncome.setAmount(income.getAmount());
            incomeRepository.save(newIncome);
            balanceServiceImpl.transaction(newIncome.getUser());
            LOGGER_WARN.warn("End update income: " + id);
        } else {
            LOGGER_ERROR.error("Income doesn't exists");
        }

    }

    public Income findIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public void deleteIncome(User user, Long id) {
        if (incomeRepository.existsById(id)) {
            LOGGER_WARN.warn("Start delete income: " + id);
            incomeRepository.deleteById(id);
            balanceServiceImpl.transaction(user);
            LOGGER_WARN.warn("End delete income: " + id);
        } else {
            LOGGER_ERROR.error("Income doesn't exists");
        }
    }


    public List<Income> findAllIncomes() {
        LOGGER_INFO.info("Read all incomes");
        return (List<Income>) incomeRepository.findAll();
    }
}
